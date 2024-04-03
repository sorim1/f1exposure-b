package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.model.enums.ExposureStatusEnum;
import sorim.f1.slasher.relentless.model.openf1.RaceControlDto;
import sorim.f1.slasher.relentless.model.strawpoll.StrawpollModelThree;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.ExposureStrawpollService;
import sorim.f1.slasher.relentless.service.FourchanService;
import sorim.f1.slasher.relentless.service.OpenF1Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExposureStrawpollServiceImpl implements ExposureStrawpollService {
    private static final String strawPollApiV3 = "https://api.strawpoll.com/v2/polls/";
    private static final Map<String, String> colorMap = new HashMap<>();
    private static boolean exposureToday = false;
    private static boolean exposureNow = false;
    private static boolean exposureReady = false;
    private static LocalDateTime exposureTime;
    private static String title = "Strange";
    private static Integer currentExposureRound;
    private static String strawpollId;
    private static Boolean showWinner = false;
    private static Integer reloadDelay = 0;
    private static Integer latestVoteCount = 0;
    private static Map<String, Driver> driversMap = new HashMap<>();
    private final ExposedVoteTotalsRepository exposedVoteTotalsRepository;
    private final ExposureChampionshipRepository exposureChampionshipRepository;
    private final ExposureChampionshipStandingsRepository exposureChampionshipStandingsRepository;
    private final DriverStandingsByRoundRepository driverStandingsByRoundRepository;
    private final DriverRepository driverRepository;
    private final CalendarRepository calendarRepository;
    private final MainProperties properties;
    private final PropertiesRepository propertiesRepository;
    private final ErgastService ergastService;
    private final FourchanService fourchanService;
    private final OpenF1Service openF1Service;
    private final RestTemplate restTemplate;

    private final JsonRepository jsonRepository;
    private final JsonRepositoryTwo jsonRepository2;

    @PostConstruct
    private void init() {
        AppProperty app = propertiesRepository.findDistinctFirstByName("exposureRound");
        if (app != null) {
            currentExposureRound = Integer.parseInt(app.getValue());
        } else {
            updateCurrentExposureRound(0);
        }
        initializeExposureFrontendVariables(null);
    }

    @Override
    public Boolean initializeExposureFrontendVariables(String id) {
        Boolean response = false;
        strawpollId = id;
        Logger.logAdmin("initializeExposureFrontendVariablesNew called");
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceBeforeOrderByRaceDesc(gmtDateTime);
        setDriverNameMap();
        if (f1calendar != null) {
            title = f1calendar.getLocation();
            Duration howMuchTimeSincePreviousRace = Duration.between(f1calendar.getRace(), gmtDateTime);
            if (howMuchTimeSincePreviousRace.toDays() < 1) {
//                if (strawpollId == null) {
//                    String newId = getExposureStrawpoll();
//                    if (newId != null) {
//                        strawpollId = newId;
//                        startPolling();
//                        response = true;
//                    }
//                }
                exposureToday = true;
            } else {
                f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
                if (f1calendar != null) {
                    Duration duration = Duration.between(gmtDateTime, f1calendar.getRace());
                    if (duration.toDays() > 0) {
                        updateCurrentExposureRound(0);
                        exposureToday = false;
                        Logger.logAdmin("exposureToday: " + exposureToday);

                    } else {
                        exposureToday = true;
                        title = f1calendar.getLocation();
                        updateCurrentExposureRound(1);
                        resetStrawpoll();
                        exposureTime = LocalDateTime.now().plus(duration).plusHours(1).plusMinutes(10);
                        Logger.logAdmin("initializeExposure-exposureToday: " + exposureToday);
                        Logger.logAdmin("initializeExposure-exposureToday exposureTime: " + exposureTime);
                        Logger.logAdmin("initializeExposure-exposureToday currentExposureRound: " + currentExposureRound);
                    }
                }
            }
        }
        return response;
    }

    @Override
    public Boolean isExposureNow() {
        return exposureNow;
    }

    @Override
    public Boolean setExposureNow(Boolean value) {
        exposureNow = value;
        return exposureNow;
    }


    @Override
    public void startPolling() {
        StrawpollModelThree newStrawpoll = fetchStrawpollResults();
        if (newStrawpoll != null && exposureOn()) {
            Boolean isVotable = updateExposureDataFromStrawpoll(newStrawpoll);
            log.info("newStrawpoll - isVotable: " + newStrawpoll.getPoll().getIs_votable());
            log.info("isVotable" + isVotable);
            if (isVotable) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @SneakyThrows
                            @Override
                            public void run() {
                                startPolling();
                            }
                        },
                        reloadDelay
                );
            } else {
                log.info("closeExposurePoll because voting is over" + isVotable);
                closeExposurePoll(true);
            }
        }
    }

    private void resetStrawpoll() {
        strawpollId = null;
        latestVoteCount = 0;
        reloadDelay = 0;
    }

    private void updateCurrentExposureRound(Integer increment) {
        Integer round;
        try {
            round = ergastService.getCurrentDriverStandings().getMrData().getStandingsTable().getStandingsLists().get(0).getRound();
            if (currentExposureRound == null || currentExposureRound <= round) {
                currentExposureRound = round + increment;
            } else {
                //nova sezona
//                currentExposureRound = round;
//                properties.checkCurrentSeasonFuture();
            }
            log.info("updateCurrentExposureRound3: " + currentExposureRound + " - " + round);
            AppProperty exposureProperty = AppProperty.builder().name("exposureRound").value(currentExposureRound.toString()).build();
            propertiesRepository.save(exposureProperty);
        } catch (Exception e) {
            log.error("updateCurrentExposureRound error", e);
            AppProperty app = propertiesRepository.findDistinctFirstByName("exposureRound");
            if (app != null) {
                currentExposureRound = Integer.parseInt(app.getValue()) + increment;
            }
        }
    }

    @Override
    public StrawpollModelThree fetchStrawpollResults() {
        try {
            StrawpollModelThree strawPoll = restTemplate
                    .getForObject(strawPollApiV3 + strawpollId, StrawpollModelThree.class);
            return strawPoll;
        } catch (Exception e) {
            log.error("fetchStrawpollResults error", e);
            strawpollId = null;
            return null;
        }
    }

    @Override
    public String initializeStrawpoll(String id) {
        reloadDelay = 20000;
        strawpollId = id;
        if (id == null) {
            initializeExposureFrontendVariables(null);
        } else {
            StrawpollModelThree newStrawpoll = fetchStrawpollResults();
            if (newStrawpoll != null) {
                log.info("strawpoll found: {} ", newStrawpoll);
                initializeExposureFrontendVariables(id);
                updateExposureDataFromStrawpoll(newStrawpoll);
            } else {
                log.info("strawpoll not found");
                return "STRAWPOLL NOT FOUND";
            }
        }
        if (strawpollId != null) {
            startPolling();
        }
        return strawpollId;
    }

    @Override
    public String setStrawpoll(String id) {
        reloadDelay = 20000;
        strawpollId = id;
        if (strawpollId != null) {
            startPolling();
        }
        return strawpollId;
    }

    @Override
    public String changeShowWinner(Boolean value) {
        String response = showWinner + " -> ";
        showWinner = value;
        response += showWinner;
        return response;
    }


    @Override
    public ExposureData getExposedChartData() {
        Boolean timeIsRight = checkIfStrawpollCanBeStarted();
        List<ExposureChampionshipStanding> standings = getExposureStandings();
        return ExposureData.builder()
                .title(title)
                .currentYear(properties.getCurrentSeasonPast())
                .showWinner(showWinner)
                .activeExposureChart(generateSingleExposureResult(properties.getCurrentSeasonPast(), currentExposureRound, true))
                .exposureChampionshipData(getExposureChampionshipData(standings))
                .standings(standings)
                .voters(getExposureVoters())
                .exposureRaces(jsonRepository2.findAllById("EXPOSURE_CURRENT_RACES").getJson())
                .timeIsRight(timeIsRight)
                .currentExposureRound(currentExposureRound)
                .build();
    }

    @Override
    public Boolean checkIfStrawpollCanBeStarted() {
        Boolean waitingForExposurePoll = exposureToday && !exposureNow;

        return waitingForExposurePoll && exposureReady;
    }

    @Override
    public JsonRepositoryModel archiveExposureData() {
        ExposureData response = getExposedChartData();
        response.setActiveExposureChart(null);
        response.setTitle("Season " + response.getCurrentYear());
        JsonRepositoryModel jrm = JsonRepositoryModel.builder()
                .id("EXPOSURE_ARCHIVE_" + response.getCurrentYear())
                .json(response).build();
        jsonRepository.save(jrm);

        saveExposureRaces();
        return jrm;
    }

    private void saveExposureRaces() {
        List<FrontendRace> races = ergastService.getRacesSoFar(String.valueOf(properties.getCurrentSeasonPast()), currentExposureRound);
        enrichRacesWithExposureWinner(races);
        JsonRepositoryTwoModel jrm2 = JsonRepositoryTwoModel.builder()
                .id("EXPOSURE_CURRENT_RACES")
                .json(races).build();
        jsonRepository2.save(jrm2);
    }

    private void enrichRacesWithExposureWinner(List<FrontendRace> races) {
        Integer season = properties.getCurrentSeasonPast();
        races.forEach(race -> {
            race.setRaceName(race.getRaceName().replace("Grand Prix", "GP"));
            List<ExposureChampionship> list = exposureChampionshipRepository.findAllByIdSeasonAndIdRoundOrderByVotesDesc(season, race.getRound());
            if (list != null && !list.isEmpty()) {
                race.setExposureWinner(list.get(0).getName());
            }
        });
    }


    @Override
    public void closeExposurePoll(Boolean showWinnerValue) {
        exposureToday = false;
        exposureNow = false;
        exposureReady = false;
        exposureChampionshipRepository.closeAllPolls();
        List<ExposureChampionshipData> exposureChampionshipData = getExposureChampionshipData(null);
        List<ExposureChampionshipStanding> exposureStandings = new ArrayList<>();
        exposureChampionshipData.forEach((v) -> {
            ExposureChampionshipStandingId id = ExposureChampionshipStandingId.builder()
                    .season(properties.getCurrentSeasonFuture())
                    .driver(v.getCode())
                    .build();
            exposureStandings.add(ExposureChampionshipStanding.builder().id(id).exposure(v.getScore()).build());
        });
        exposureChampionshipStandingsRepository.saveAll(exposureStandings);
        exposureChampionshipStandingsRepository.updateChampionshipNames();
        showWinner = showWinnerValue;
        backupExposureToDatabase();
        resetStrawpoll();
        try {
            saveExposureRaces();
        } catch (Exception e) {
            //TODO delete ako radi ok
            log.error("saveExposureRaces error", e);
        }

    }

    private void backupExposureToDatabase() {
        try {
            log.info("backupExposureToDatabase start");
            FullExposure fullExposure = backupExposure();
            JsonRepositoryModel fullExposureJson = JsonRepositoryModel.builder()
                    .id("EXPOSURE_" + currentExposureRound + "_" + strawpollId)
                    .json(fullExposure).build();
            jsonRepository.save(fullExposureJson);
            log.info("backupExposureToDatabase end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ExposureChampionshipStanding> getExposureStandings() {
        return exposureChampionshipStandingsRepository.findAllByIdSeasonOrderByExposureDesc(properties.getCurrentSeasonPast());
    }

    private List<Integer> getExposureVoters() {
        return exposedVoteTotalsRepository.getVoterCountOfSeason(properties.getCurrentSeasonPast());
    }

    @Override
    public ActiveExposureChart generateSingleExposureResult(Integer season, Integer round, boolean detailed) {
        List<String> drivers = new ArrayList<>();
        List<String> driverNames = new ArrayList<>();
        List<Integer> results = new ArrayList<>();
        List<BigDecimal> exposureList = new ArrayList<>();
        List<ExposureChampionship> list = exposureChampionshipRepository.findAllByIdSeasonAndIdRoundOrderByVotesDesc(season, round);
        list.forEach((row) -> {
            drivers.add(row.getId().getDriver());
            driverNames.add(row.getName());
            exposureList.add(row.getExposure());
            if (detailed) {
                results.add(row.getVotes());
            }
        });
        ActiveExposureChart response = ActiveExposureChart.builder()
                .drivers(drivers.toArray(new String[drivers.size()]))
                .driverNames(driverNames.toArray(new String[driverNames.size()]))
                .exposure(exposureList.toArray(new BigDecimal[results.size()]))
                .round(round)
                .season(season)
                .build();
        if (detailed) {
            ExposedVoteTotals total = exposedVoteTotalsRepository.findExposedTotalBySeasonAndRound(season, round);
            if (total == null) {
                total = new ExposedVoteTotals();
            }
            response.setResults(results.toArray(new Integer[results.size()]));
            response.setVotes(total.getVotes());
            response.setVoters(total.getVoters());
            response.setStrawpoll(total.getStrawpoll());
            response.setDelay(getReloadDelay());
        }
        return response;
    }

    @Override
    public KeyValue getLatestRaceExposureWinner() {
        //TODO remove trycatch ako je sve ok properties.getCurrentSeasonPast(), currentExposureRound
        ExposureChampionship winner = exposureChampionshipRepository.findFirstByIdSeasonAndIdRoundOrderByVotesDesc(properties.getCurrentSeasonPast(), currentExposureRound);
        if (winner != null && winner.getVotes() > 20) {
            String key = title;
            String value = winner.getId().getDriver();
            showWinner = false;
            return KeyValue.builder().key(key).value(value).build();
        } else {
            showWinner = true;
        }
        return null;
    }

    @Override
    public Object getSingleExposureResult(Integer season, Integer round) {
        JsonRepositoryModel jrm = jsonRepository.findAllById("EXPOSURE_RESULT_" + season + "_" + round);
        if (jrm != null) {
            log.info("nasao single exposure result u bazi");
            return jrm.getJson();
        } else {
            ActiveExposureChart generatedExposureResult = generateSingleExposureResult(season, round, false);
            JsonRepositoryModel saveData = JsonRepositoryModel.builder()
                    .id("EXPOSURE_RESULT_" + season + "_" + round)
                    .json(generatedExposureResult).build();
            jsonRepository.save(saveData);
            log.info("spremio single exposure result u bazi");
            return generatedExposureResult;
        }
    }

    @Override
    public void raceHasStarted() {
        log.info("raceHasStarted");
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        log.info(" - PROŠLO JE SAT VREMENA UTRKE: ");
                        exposureReady = true;
                        checkRaceStatusUsingOpenF1Service();
                    }

                },
                3600000
        );
    }

    private void checkRaceStatusUsingOpenF1Service() {
        List<RaceControlDto> response;
        int counter = 0;
        try {
            do {
                log.info("POZIVAM getTodayRaceControlData: {}", counter++);
                response = openF1Service.getTodayRaceControlData("CHEQUERED");
                Thread.sleep(120000);
            } while (!response.isEmpty() && counter < 60);
            log.info("CHEQUERED flag found: {}", response.size());
        } catch (Exception e) {
           log.error("error checkRaceStatusUsingOpenF1Service ", e);
        }
    }

    private Integer getReloadDelay() {
        if (exposureOn()) {
            return reloadDelay;
        }
        return 0;
    }

    private List<ExposureChampionshipData> getExposureChampionshipData(List<ExposureChampionshipStanding> standings) {
        List<ExposureChampionship> rawData = exposureChampionshipRepository.findAllByIdSeasonAndStatusOrderByIdRound(properties.getCurrentSeasonPast(), 3);
        Map<String, ExposureChampionshipData> map = new TreeMap<>();
        rawData.forEach(row -> {
            if (map.containsKey(row.getId().getDriver())) {
                ExposureChampionshipData data = map.get(row.getId().getDriver());
                List<BigDecimal> newResult = new ArrayList<>();
                newResult.add(BigDecimal.valueOf(row.getId().getRound()));
                newResult.add(row.getExposure());
                BigDecimal newScore = data.getScore().add(row.getExposure());
                List<BigDecimal> newStanding = new ArrayList<>();
                newStanding.add(BigDecimal.valueOf(row.getId().getRound()));
                newStanding.add(newScore);
                data.getScoresByRound().add(newResult);
                data.getScoresThroughRounds().add(newStanding);
                data.updateMaxExposure(row.getId().getRound(), row.getExposure());
                data.setScore(newScore);

            } else {
                ExposureChampionshipData data = new ExposureChampionshipData();
                data.setCode(row.getId().getDriver());
                data.setColor(row.getColor());
                List<BigDecimal> newResult = new ArrayList<>();
                newResult.add(BigDecimal.valueOf(row.getId().getRound()));
                newResult.add(row.getExposure());
                data.getScoresByRound().add(newResult);
                data.getScoresThroughRounds().add(newResult);
                data.setScore(row.getExposure());
                data.setMaxExposure(row.getExposure());
                data.setMaxExposureRound(row.getId().getRound());
                map.put(row.getId().getDriver(), data);
            }
        });
        if (standings == null) {
            return new ArrayList<>(map.values());
        } else {
            List<ExposureChampionshipData> output = new ArrayList<>();
            standings.forEach(standing -> {
                if (map.containsKey(standing.getId().getDriver())) {
                    output.add(map.get(standing.getId().getDriver()));
                }
            });
            return output;
        }
    }

    private Boolean updateExposureDataFromStrawpoll(StrawpollModelThree strawpoll) {
        List<ExposureChampionship> list = new ArrayList<>();
        Integer voters = strawpoll.getPoll().getPoll_meta().getParticipant_count();
        if (voters > 0) {
            Integer totalVotes = strawpoll.getPoll().getPoll_meta().getVote_count();
            if (totalVotes > latestVoteCount) {
                reloadDelay = 20000;
            } else {
                reloadDelay = reloadDelay + 20000;
                log.info("reloadDelay increased:" + reloadDelay);
            }
            latestVoteCount = totalVotes;
            strawpoll.getPoll().getPoll_options().forEach(pollAnswer -> {
                String code = getDriverCodeFromName(pollAnswer.getValue());
                SeasonRoundDriverId exposedId = SeasonRoundDriverId.builder()
                        .season(properties.getCurrentSeasonFuture())
                        .round(currentExposureRound)
                        .driver(code).build();

                BigDecimal exposure = new BigDecimal(pollAnswer.getVote_count() * 100).divide(new BigDecimal(voters), 2, RoundingMode.HALF_UP);
                ExposureChampionship newRow = ExposureChampionship.builder().id(exposedId)
                        .exposure(exposure)
                        .color(getColorFromDriverCode(code))
                        .status(2)
                        .name(pollAnswer.getValue())
                        .votes(pollAnswer.getVote_count()).build();
                list.add(newRow);
            });
            exposureChampionshipRepository.saveAll(list);
            ExposedTotalsId totalsId = ExposedTotalsId.builder().season(properties.getCurrentSeasonFuture())
                    .round(currentExposureRound).build();
            ExposedVoteTotals totals = ExposedVoteTotals.builder().id(totalsId).voters(voters).votes(totalVotes)
                    .strawpoll(strawpollId)
                    .build();
            exposedVoteTotalsRepository.save(totals);
        }
        return strawpoll.getPoll().getIs_votable();
    }


    private String getColorFromDriverCode(String code) {
        if (colorMap.containsKey(code)) {
            return colorMap.get(code);
        } else {
            DriverStandingByRound dsbr = driverStandingsByRoundRepository.findFirstByCodeOrderByIdSeasonDesc(code);
            String color = null;
            if (dsbr != null) {
                color = dsbr.getColor();
                colorMap.put(code, color);
                return color;
            }

        }
        return "#000000";
    }

    private String getDriverCodeFromName(String name) {
        if (driversMap.containsKey(name)) {
            return driversMap.get(name).getCode();
        } else {
            Driver newDriver = Driver.builder().status(1).fullName(name)
                    .code(name.substring(0, 3).toUpperCase()).ergastCode(name.toLowerCase()).build();
            log.info("getDriverCodeFromName - save driver list - one: " + name);
            if (newDriver.getCode().equals("PER")) {
                log.error("PEREZ NIJE U MAPI - ZASTO?? ");
                newDriver.setErgastCode("perez");
            }
            driverRepository.save(newDriver);
            driversMap.put(name, newDriver);
            return newDriver.getCode();
        }
    }

    private void setDriverNameMap() {
        List<Driver> list = driverRepository.findAll();
        driversMap = list.stream()
                .collect(Collectors.toMap(Driver::getFullName, Function.identity()));
    }

    @Override
    public ExposureResponse getExposureDriverList() {
        ExposureResponse response = ExposureResponse.builder()
                .title(title)
                .year(properties.getCurrentSeasonPast())
                .exposureTime(exposureTime)
                .exposureNow(exposureNow)
                .exposureToday(exposureToday)
                .currentRound(currentExposureRound)
                .build();
        if (exposureOn()) {
            // if(true) {
            List<Driver> drivers = driverRepository.findAllByStatusOrderByFullName(1);
            response.setDrivers(drivers);
            response.setStatus(ExposureStatusEnum.ACTIVE);
        } else {
            if (exposureToday) {
                response.setStatus(ExposureStatusEnum.SOON);
            } else {
                response.setStatus(ExposureStatusEnum.OVER);
            }

        }
        return response;
    }

    @Override
    public boolean exposureOn() {
        if (exposureNow) {
            return true;
        }
        if (!exposureToday) {
            return false;
        }
//        if (LocalDateTime.now().isAfter(exposureTime)) {
//            exposureNow = true;
//            return true;
//        }

        return false;
    }

    public void setCurrentExposureRound(Integer newCurrentRound) {
        currentExposureRound = newCurrentRound;
        AppProperty exposureProperty = AppProperty.builder().name("exposureRound").value(currentExposureRound.toString()).build();
        propertiesRepository.save(exposureProperty);
    }

    @Override
    public FullExposure backupExposure() {
        return FullExposure.builder()
                .exposedVoteTotals((List<ExposedVoteTotals>) exposedVoteTotalsRepository.findAll())
                .exposureChampionship((List<ExposureChampionship>) exposureChampionshipRepository.findAll())
                .exposureChampionshipStandings((List<ExposureChampionshipStanding>) exposureChampionshipStandingsRepository.findAll())
                .build();
    }

    @Override
    public Boolean restoreExposureFromBackup(FullExposure fullExposure) {
        exposureChampionshipRepository.deleteAll();
        exposureChampionshipRepository.saveAll(fullExposure.getExposureChampionship());
        exposureChampionshipStandingsRepository.deleteAll();
        exposureChampionshipStandingsRepository.saveAll(fullExposure.getExposureChampionshipStandings());
        exposedVoteTotalsRepository.deleteAll();
        exposedVoteTotalsRepository.saveAll(fullExposure.getExposedVoteTotals());
        return true;
    }

    @Override
    public void openExposurePoll(Integer minutes) {
        exposureToday = true;
        exposureTime = LocalDateTime.now().plusMinutes(minutes);
    }

    @Override
    public List<Integer> incrementExposureRound() {
        List<Integer> response = new ArrayList<>();
        response.add(currentExposureRound);
        currentExposureRound++;
        response.add(currentExposureRound);
        AppProperty exposureProperty = AppProperty.builder().name("exposureRound").value(currentExposureRound.toString()).build();
        propertiesRepository.save(exposureProperty);
        return response;
    }

    @Override
    public List<Integer> setCurrentRound(Integer newRound) {
        List<Integer> response = new ArrayList<>();
        response.add(currentExposureRound);
        currentExposureRound = newRound;
        response.add(currentExposureRound);
        AppProperty exposureProperty = AppProperty.builder().name("exposureRound").value(currentExposureRound.toString()).build();
        propertiesRepository.save(exposureProperty);
        return response;
    }

    @Override
    public Integer getCurrentRoundUp() {
        return currentExposureRound;
    }

    @Override
    public String getExposureStrawpoll() {
        return fourchanService.getExposureStrawpoll();
    }

    @Override
    public Integer resetLatestPoll() {
        return exposureChampionshipRepository.deleteByIdSeasonAndIdRoundOrderByVotesDesc(properties.getCurrentSeasonFuture(), currentExposureRound);
    }
}
