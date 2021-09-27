package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.CalendarData;
import sorim.f1.slasher.relentless.model.FullExposure;
import sorim.f1.slasher.relentless.model.SportSurge;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.scheduled.Scheduler;
import sorim.f1.slasher.relentless.service.AdminService;
import sorim.f1.slasher.relentless.service.ClientService;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.ExposureService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminServiceImpl implements AdminService {

    private static Integer CURRENT_ROUND;

    private final CalendarRepository calendarRepository;
    private final DriverStandingsRepository driverStandingsRepository;
    private final ConstructorStandingsRepository constructorStandingsRepository;
    private final DriverStandingsByRoundRepository driverStandingsByRoundRepository;
    private final ConstructorStandingsByRoundRepository constructorStandingsByRoundRepository;
    private final DriverRepository driverRepository;
    private final SportSurgeStreamRepository sportSurgeStreamRepository;
    private final SportSurgeEventRepository sportSurgeEventRepository;
    private final PropertiesRepository propertiesRepository;
    private final AwsRepository awsRepository;
    private final AwsCommentRepository awsCommentRepository;
    private final F1CommentRepository f1CommentRepository;

    private final ErgastService ergastService;
    private final MainProperties properties;
    private final ClientService clientService;
    private final ExposureService exposureService;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void onInit() {
        CURRENT_ROUND = Integer.valueOf(propertiesRepository.findDistinctFirstByName("round").getValue());
    }

    @Override
    public void initialize() throws Exception {
        refreshCalendarOfCurrentSeason();
        initializeStandings();
        fetchSportSurgeLinks();
    }

    @Override
    public void refreshCalendarOfCurrentSeason() throws Exception {
        URL url = new URL(properties.getCalendarUrl());
        Reader r = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(r);
        int raceId = 0;
        int ergastRound = -1;
        int currentRaceId;
        List<F1Calendar> f1calendarList = new ArrayList<>();
        F1Calendar f1Calendar = null;
        List<RaceData> ergastRaceData = ergastService.fetchSeason(String.valueOf(properties.getCurrentYear()));
        for (CalendarComponent component : calendar.getComponents().getAll()) {
            if (component.getName().equals("VEVENT")) {
                PropertyList properties = component.getProperties();
                String[] idAndRound = properties.get("UID").get(0).getValue().split("@");
                currentRaceId = Integer.parseInt(idAndRound[1]);
                if (currentRaceId == raceId) {
                    f1Calendar.setDateAndNameFromRoundDescription(idAndRound[0], properties.get("DTSTART").get(0).getValue(), properties.get("SUMMARY").get(0).getValue());
                } else {
                    if (f1Calendar != null) {
                        f1calendarList.add(f1Calendar);
                    }
                    raceId = currentRaceId;
                    if (ergastRound == -1) {
                        f1Calendar = new F1Calendar(properties);
                        ergastRound++;
                    } else {
                        if (ergastRound < ergastRaceData.size()) {
                            f1Calendar = new F1Calendar(properties);
                        } else {
                            f1Calendar = new F1Calendar(properties);
                        }
                    }
                }
            }
        }
        enrichCalendarWithErgastData(f1calendarList, ergastRaceData );
        calendarRepository.saveAll(f1calendarList);

    }

    private void enrichCalendarWithErgastData(List<F1Calendar> f1calendarList, List<RaceData> ergastRaceData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer ergastElement = 0;
        Integer calendarElement = 0;
        do{
            LocalDateTime calendarDate = f1calendarList.get(calendarElement).getRace();
            LocalDate ergastDate = LocalDate.parse(ergastRaceData.get(ergastElement).getDate(), formatter);
            if(calendarDate!=null) {
                if (calendarDate.getDayOfYear() == ergastDate.getDayOfYear()) {
                    log.info("isti dan: {} - {} - {} ", ergastElement, calendarElement, ergastRaceData.get(ergastElement).getRaceName());
                    f1calendarList.get(calendarElement).setErgastName("MAYBE: " + ergastRaceData.get(ergastElement).getRaceName());
                    f1calendarList.get(calendarElement).setErgastDateTime(ergastRaceData.get(ergastElement).getDate() + " - " + ergastRaceData.get(ergastElement).getTime());
                    ergastElement++;
                    calendarElement++;
                } else if (calendarDate.getDayOfYear() > ergastDate.getDayOfYear()) {
                    log.info("kalendar veći: {} - {} - {} ", ergastElement, calendarElement, ergastRaceData.get(ergastElement).getRaceName());
                    ergastElement++;
                } else if (calendarDate.getDayOfYear() < ergastDate.getDayOfYear()) {
                    log.info("kalendar manji: {} - {} - {} ", ergastElement, calendarElement, ergastRaceData.get(ergastElement).getRaceName());
                    calendarElement++;
                }
            } else {
                calendarElement++;
            }
        }while(calendarElement<f1calendarList.size()-1 || ergastElement<ergastRaceData.size()-1);
    }


    @Override
    public void validateCalendarForNextRace() throws Exception {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
        String url = properties.getFormula1RacingUrl() + properties.getCurrentYear() + ".html";
        String rawHtml = restTemplate
                .getForObject(url, String.class);
        //todo TBC
        Document doc = Jsoup.parse(rawHtml);
        Elements clock = doc.getAllElements();
        log.info("clock.wholeText()");
        //Elements tRows1 = clock.getElementsByClass("f1-color--white countdown-text");
//        clock.forEach(row -> {
//            log.info("row.wholeText()1");
//            log.info(row.wholeText());
//            row.getAllElements().forEach(row2 -> {
//                log.info("row2.wholeText()1");
//                log.info(row2.wholeText());
//            });
//        });
    }

    @Override
    public Boolean initializeStandings() {
        Logger.log("initializeStandings");
        Boolean changesDetected = refreshDriverStandingsFromErgast();
        if(changesDetected){
            Scheduler.standingsUpdated = true;
        }
        initializeConstructorStandings();
        return changesDetected;
    }

    @Override
    public Boolean initializeFullStandingsThroughRounds() {
        Logger.log("initializeFullStandingsThroughRounds");
        boolean iterate;
        Integer round = 1;
        Map<String, DriverStandingByRound> driverStandingsByRound = new HashMap<>();
        do {

            ErgastResponse response = ergastService.getDriverStandingsByRound(properties.getCurrentYear(), round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                        .forEach(ergastStanding -> {
                            driverStandingsByRound.put(ergastStanding.getDriver().getDriverId() + finalRound, new DriverStandingByRound(ergastStanding, properties.getCurrentYear(), finalRound));
                        });
                round++;
                iterate = true;
            } else {
                iterate = false;
            }
        } while (iterate);
        round = 1;

        Map<String, ConstructorStandingByRound> constructorStandingByRound = new HashMap<>();
        do {
            ErgastResponse response = ergastService.getConstructorStandingsByRound(properties.getCurrentYear(), round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                        .forEach(ergastStanding -> {
                            constructorStandingByRound.put(ergastStanding.getConstructor().getConstructorId() + finalRound, new ConstructorStandingByRound(ergastStanding, properties.getCurrentYear(), finalRound));

                        });
                round++;
                iterate = true;
            } else {
                iterate = false;
            }
        } while (iterate);
        enrichFullStandingsWithRoundPoints(driverStandingsByRound, constructorStandingByRound);
        driverStandingsByRoundRepository.deleteAll();
        driverStandingsByRoundRepository.saveAll(driverStandingsByRound.values());
        constructorStandingsByRoundRepository.deleteAll();
        constructorStandingsByRoundRepository.saveAll(constructorStandingByRound.values());
        return true;
    }

    private void enrichFullStandingsWithRoundPoints(Map<String, DriverStandingByRound> driverStandingsByRound, Map<String, ConstructorStandingByRound> constructorStandingByRound) {
        boolean iterate;
        Integer round = 1;
        do {
            ErgastResponse response = ergastService.getResultsByRound(properties.getCurrentYear(), round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                response.getMrData().getRaceTable().getRaces().get(0).getResults()
                        .forEach(ergastStanding -> {
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId() + finalRound).setPointsThisRound(ergastStanding.getPoints());
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId() + finalRound).setResultThisRound(ergastStanding.getPosition());
                            constructorStandingByRound.get(ergastStanding.getConstructor().getConstructorId() + finalRound).incrementPointsThisRound(ergastStanding.getPoints());
                        });
                round++;
                iterate = true;
            } else {
                iterate = false;
            }
        } while (iterate);
    }

    private void enrichSingleRoundStandingsWithRoundPoints(Map<String, DriverStandingByRound> driverStandingsByRound, Map<String, ConstructorStandingByRound> constructorStandingByRound) {
        ErgastResponse response = ergastService.getResultsByRound(properties.getCurrentYear(), CURRENT_ROUND);
        if (response.getMrData().getTotal() > 0) {
            response.getMrData().getRaceTable().getRaces().get(0).getResults()
                    .forEach(ergastStanding -> {
                        if (driverStandingsByRound != null) {
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId()).setPointsThisRound(ergastStanding.getPoints());
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId()).setResultThisRound(ergastStanding.getPosition());
                        }
                        if (constructorStandingByRound != null) {
                            constructorStandingByRound.get(ergastStanding.getConstructor().getConstructorId()).incrementPointsThisRound(ergastStanding.getPoints());
                        }
                    });
        }
    }

    private Boolean refreshDriverStandingsFromErgast() {
        Boolean bool = false;
        List<DriverStanding> driverStandings = new ArrayList<>();
        Map<String, DriverStandingByRound> driverStandingsByRound = new HashMap<>();
        ErgastResponse response = ergastService.getDriverStandings();
        if (response.getMrData().getStandingsTable().getStandingsLists().get(0).getRound() != CURRENT_ROUND) {
            CURRENT_ROUND = response.getMrData().getStandingsTable().getStandingsLists().get(0).getRound();
            propertiesRepository.updateProperty("round", CURRENT_ROUND.toString());
            Logger.log("initializeStandings - changes detected");
            bool = true;
        } else {
            Logger.log("initializeStandings - no changes detected");
        }
        response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                .forEach(ergastStanding -> {
                    driverStandings.add(new DriverStanding(ergastStanding));
                    driverStandingsByRound.put(ergastStanding.getDriver().getDriverId(), new DriverStandingByRound(ergastStanding, properties.getCurrentYear(), CURRENT_ROUND));
                });
        response = ergastService.getResultsByRound(properties.getCurrentYear(), CURRENT_ROUND);
        response.getMrData().getRaceTable().getRaces().get(0).getResults()
                .forEach(ergastStanding -> {
                    driverStandingsByRound.get(ergastStanding.getDriver().getDriverId()).incrementPointsThisRound(ergastStanding.getPoints());
                });
        driverStandingsRepository.deleteAll();
        driverStandingsRepository.saveAll(driverStandings);
        enrichSingleRoundStandingsWithRoundPoints(driverStandingsByRound, null);
        driverStandingsByRoundRepository.saveAll(driverStandingsByRound.values());
        updateExposureDriverList(driverStandings);
        return bool;
    }


    private void updateExposureDriverList(List<DriverStanding> driverStandings) {
        //poveznica između exposure liste i standings liste
        List<ExposureDriver> drivers = driverRepository.findAll();
        exposureService.setNextRoundOfExposure(driverStandings, CURRENT_ROUND + 1);
        for (DriverStanding driverStanding : driverStandings) {
            Optional<ExposureDriver> foundDriver = drivers.stream().filter(x -> driverStanding.getName().equals(x.getFullName()))
                    .findFirst();
            if (foundDriver.isPresent()) {
            } else {
                ExposureDriver newDriver = ExposureDriver.builder()
                        .fullName(driverStanding.getName())
                        .code(driverStanding.getCode())
                        .ergastCode(driverStanding.getErgastCode())
                        .status(1)
                        .build();
                driverRepository.save(newDriver);
            }
        }
    }

    private void initializeConstructorStandings() {
        List<ConstructorStanding> constructorStandings = new ArrayList<>();
        Map<String, ConstructorStandingByRound> constructorStandingsByRound = new HashMap<>();
        ErgastResponse response = ergastService.getConstructorStandings();
        response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                .forEach(ergastStanding -> {
                    constructorStandings.add(new ConstructorStanding(ergastStanding));
                    constructorStandingsByRound.put(ergastStanding.getConstructor().getConstructorId(), new ConstructorStandingByRound(ergastStanding, properties.getCurrentYear(), CURRENT_ROUND));
                });
        constructorStandingsRepository.deleteAll();
        constructorStandingsRepository.saveAll(constructorStandings);
        enrichSingleRoundStandingsWithRoundPoints(null, constructorStandingsByRound);
        constructorStandingsByRoundRepository.saveAll(constructorStandingsByRound.values());
    }

    @Override
    public Integer fetchSportSurgeLinks() throws IOException {
        Logger.log("fetchSportSurgeLinks");
        WebClient client = new WebClient();
        Page page = client.getPage(properties.getSportSurgeRoot());
        WebResponse response = page.getWebResponse();
        SportSurge sportSurge = new ObjectMapper().readValue(response.getContentAsString(), SportSurge.class);
        List<SportSurgeEvent> events = sportSurge.getEvents();
        List<SportSurgeStream> streams = new ArrayList<>();
        for (SportSurgeEvent event : events) {
            page = client.getPage(properties.getSportSurgeStreams() + event.getId());
            response = page.getWebResponse();
            sportSurge = new ObjectMapper().readValue(response.getContentAsString(), SportSurge.class);
            streams.addAll(sportSurge.getStreams());
        }
        deleteSportSurgeLinks();
        sportSurgeStreamRepository.saveAll(streams);
        sportSurgeEventRepository.saveAll(events);
        return getNextRefreshTick(600);
    }

    @Override
    public void deleteSportSurgeLinks() {
        Logger.log("deleteSportSurgeLinks");
        sportSurgeStreamRepository.deleteAll();
        sportSurgeEventRepository.deleteAll();
    }

    @Override
    public void closeExposurePoll() {
        Logger.log("closeExposurePoll");
        exposureService.closeExposurePoll();
    }

    @Override
    public void openExposurePoll(Integer minutes) {
        Logger.log("openExposurePoll");
        exposureService.openExposurePoll(minutes);
    }

    @Override
    public F1Calendar getUpcomingRace() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        return calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
    }

    @Override
    public F1Calendar updateUpcomingRace(F1Calendar entry) {
        calendarRepository.save(entry);
        return entry;
    }

    @Override
    public Integer deleteComment(Integer mode, Integer id) {
        switch (mode) {
            case 1:
                return f1CommentRepository.updateStatus(id, 1);
            case 2:
                return awsCommentRepository.updateStatus(id, 2);
        }
        return -1;
    }

    @Override
    public AwsContent patchAwsPost(AwsContent entry) {
        return awsRepository.save(entry);
    }

    @Override
    public List<Integer> updateCurrentRound(boolean increaseOnly) {
        List<Integer> response = new ArrayList<>();
        response.add(CURRENT_ROUND);
        refreshDriverStandingsFromErgast();
        response.add(CURRENT_ROUND);
        if (increaseOnly) {
            exposureService.setCurrentExposureRound(CURRENT_ROUND);
        }
        return response;
    }

    @Override
    public List<Integer> setCurrentRound(Integer newRound) {
        List<Integer> response = new ArrayList<>();
        response.add(CURRENT_ROUND);
        CURRENT_ROUND = newRound;
        propertiesRepository.updateProperty("round", CURRENT_ROUND.toString());
        response.add(CURRENT_ROUND);
        exposureService.setCurrentExposureRound(CURRENT_ROUND);
        return response;
    }

    @Override
    public FullExposure backupExposure() {
        return exposureService.backupExposure();
    }

    @Override
    public Integer getNextRefreshTick(Integer seconds) {
        try {
            CalendarData calendarData = clientService.getCountdownData(0);
            if (calendarData.getCountdownData().get("raceDays") > 2) {
                return null;
            }
            if (calendarData.getCountdownData().get("FP1Seconds") > seconds) {
                return calendarData.getCountdownData().get("FP1Seconds") - seconds;
            }
            if (calendarData.getCountdownData().get("FP2Seconds") > seconds) {
                return calendarData.getCountdownData().get("FP2Seconds") - seconds;
            }
            if (calendarData.getCountdownData().get("FP3Seconds") > seconds) {
                return calendarData.getCountdownData().get("FP3Seconds") - seconds;
            }
            if (calendarData.getCountdownData().get("qualifyingSeconds") > seconds) {
                return calendarData.getCountdownData().get("qualifyingSeconds") - seconds;
            }
            if (calendarData.getCountdownData().get("raceSeconds") > seconds) {
                return calendarData.getCountdownData().get("raceSeconds") - seconds;
            }
        } catch (Exception e) {
            Logger.log("getNextRefreshTick", e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public List<F1Comment> getAdminMessages() {
        return f1CommentRepository.findFirst30ByPageAndStatusOrderByTimestampDesc(47,1);
    }

    @Override
    public Boolean endRaceWeekendJobs() {
        Scheduler.analysisDone = true;
        Scheduler.standingsUpdated = true;
        return true;
    }

    @Override
    public Boolean restoreExposureFromBackup(FullExposure fullExposure) {
        return exposureService.restoreExposureFromBackup(fullExposure);
    }

    @Override
    public List<ExposureDriver> getExposureDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public List<ExposureDriver> updateExposureDrivers(List<ExposureDriver> list) {
        driverRepository.saveAll(list);
        return driverRepository.findAll();
    }
}
