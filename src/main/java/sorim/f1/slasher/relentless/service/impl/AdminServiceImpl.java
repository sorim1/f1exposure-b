package sorim.f1.slasher.relentless.service.impl;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.scheduled.Scheduler;
import sorim.f1.slasher.relentless.service.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    private final PropertiesRepository propertiesRepository;
    private final NewsRepository newsRepository;
    private final NewsCommentRepository newsCommentRepository;
    private final F1CommentRepository f1CommentRepository;
    private final JsonRepository jsonRepository;
    private final JsonRepositoryTwo jsonRepositoryTwo;
    private final InstagramService instagramService;
    private final RedditService redditService;
    private final TwitterService twitterService;
    private final TwitchService twitchService;
    private final FourchanService fourchanService;
    private final ErgastService ergastService;
    private final MainProperties properties;
    private final ClientService clientService;
    private final ExposureStrawpollService exposureService;
    private final MarketingService marketingService;
    private final ArtService artService;
    private final VideoService videoService;

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @PostConstruct
    public void onInit() {
        CURRENT_ROUND = Integer.valueOf(propertiesRepository.findDistinctFirstByName("round").getValue());
    }

    @Override
    public void initialize() throws Exception {
        //refreshCalendarOfCurrentSeason(null);
        ergastService.fetchCurrentDrivers();
        initializeStandings(true);
    }

    @Override
    public Boolean deleteCalendar() throws Exception {
        calendarRepository.deleteAll();
        return true;
    }

    @Override
    public Boolean refreshCalendarOfCurrentSeason(String urlString) throws Exception {
        URL url;
        if (urlString == null) {
            url = new URL(properties.getCalendarUrl());
        } else {
            log.info("custom url: " + urlString);
            url = new URL(urlString);
        }
        Reader r = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(r);
        int raceId = 0;
        int currentRaceId;
        List<F1Calendar> f1calendarList = new ArrayList<>();
        F1Calendar f1Calendar = null;
        for (CalendarComponent component : calendar.getComponents().getAll()) {
            if (component.getName().equals("VEVENT") && component.getProperties().get("STATUS").get(0).getValue().equals("CONFIRMED")) {
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
                    f1Calendar = new F1Calendar(properties, true);
                }
            }
        }
        f1calendarList.add(f1Calendar);
        if (f1Calendar != null && f1Calendar.getRace() != null) {
            List<RaceData> ergastRaceData = ergastService.fetchSeason(String.valueOf(f1Calendar.getRace().getYear()));
            enrichCalendarWithErgastData(f1calendarList, ergastRaceData);
        }
        calendarRepository.saveAll(f1calendarList);
        return true;
    }

    @Override
    public Boolean refreshCalendarOfCurrentSeasonSecondary(String urlString) throws Exception {
        URL url;
        if (urlString == null) {
            url = new URL(properties.getCalendarUrl());
        } else {
            log.info("custom url: " + urlString);
            url = new URL(urlString);
        }
        Reader r = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(r);
        String location = "0";
        String currentLocation;
        List<F1Calendar> f1calendarList = new ArrayList<>();
        F1Calendar f1Calendar = null;
        for (CalendarComponent component : calendar.getComponents().getAll()) {
            if (component.getName().equals("VEVENT") && !component.getProperties().get("STATUS").get(0).getValue().equals("CANCELLED")) {
                PropertyList properties = component.getProperties();
                currentLocation = properties.get("LOCATION").get(0).getValue();
                if (currentLocation.equals(location)) {
                    f1Calendar.setDateAndNameFromCategory(properties.get("CATEGORIES").get(0).getValue(), properties.get("DTSTART").get(0).getValue(), properties.get("SUMMARY").get(0).getValue());
                } else {
                    if (f1Calendar != null) {
                        f1calendarList.add(f1Calendar);
                    }
                    location = currentLocation;
                    f1Calendar = new F1Calendar(properties, false);
                }
            }
        }
        f1calendarList.add(f1Calendar);
        if (f1Calendar != null && f1Calendar.getRace() != null) {
            try{
                List<RaceData> ergastRaceData = ergastService.fetchSeason(String.valueOf(f1Calendar.getRace().getYear()));
                enrichCalendarWithErgastData(f1calendarList, ergastRaceData);
            }catch(Exception ex){
                log.error("errow while fetching ergast for calendar", ex);
            }
        }
        calendarRepository.deleteAll();
        calendarRepository.saveAll(f1calendarList);
        return true;
    }

    private void enrichCalendarWithErgastData(List<F1Calendar> f1calendarList, List<RaceData> ergastRaceData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer ergastElement = 0;
        Integer calendarElement = 0;
        do {
            LocalDateTime calendarDate = f1calendarList.get(calendarElement).getRace();
            LocalDate ergastDate = LocalDate.parse(ergastRaceData.get(ergastElement).getDate(), formatter);
            if (calendarDate != null) {
                if (calendarDate.getDayOfYear() == ergastDate.getDayOfYear()) {
                    log.info("isti dan: {} - {} - {} ", ergastElement, calendarElement, ergastRaceData.get(ergastElement).getRaceName());
                    f1calendarList.get(calendarElement).setErgastName(ergastRaceData.get(ergastElement).getRaceName());
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
        } while (calendarElement < f1calendarList.size() - 1 || ergastElement < ergastRaceData.size() - 1);
    }

    @Override
    public Boolean initializeStandings(Boolean updateStatistics) {
        Logger.log("initializeStandings");
        Boolean changesDetected = refreshDriverStandingsFromErgast();
        initializeConstructorStandings();
        if (changesDetected) {
            Scheduler.standingsUpdated = true;
        }
        if (updateStatistics) {
            ergastService.fetchStatisticsFullFromPartial(false);
        }
        generateChart();
        return changesDetected;
    }

    @Override
    public Boolean initializeFullStandingsThroughRounds() {
        Logger.log("initializeFullStandingsThroughRounds");
        boolean iterate;
        Integer round = 1;
        Map<String, DriverStandingByRound> driverStandingsByRound = new HashMap<>();
        do {

            ErgastResponse response = ergastService.getDriverStandingsByRound(properties.getCurrentSeasonPast(), round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                        .forEach(ergastStanding -> {
                            driverStandingsByRound.put(ergastStanding.getDriver().getDriverId() + finalRound, new DriverStandingByRound(ergastStanding, properties.getCurrentSeasonPast(), finalRound));
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
            ErgastResponse response = ergastService.getConstructorStandingsByRound(properties.getCurrentSeasonPast(), round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                        .forEach(ergastStanding -> {
                            constructorStandingByRound.put(ergastStanding.getConstructor().getConstructorId() + finalRound, new ConstructorStandingByRound(ergastStanding, properties.getCurrentSeasonPast(), finalRound));

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
        generateChart();
        return true;
    }

    private void enrichFullStandingsWithRoundPoints(Map<String, DriverStandingByRound> driverStandingsByRound, Map<String, ConstructorStandingByRound> constructorStandingByRound) {
        boolean iterate;
        Integer round = 1;
        do {
            ErgastResponse response = ergastService.getResultsByRound(properties.getCurrentSeasonPast(), round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                Integer maxPosition = response.getMrData().getRaceTable().getRaces().get(0).getResults().size();
                response.getMrData().getRaceTable().getRaces().get(0).getResults()
                        .forEach(ergastStanding -> {
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId() + finalRound).setDataFromARound(ergastStanding, maxPosition);
                            constructorStandingByRound.get(ergastStanding.getConstructor().getConstructorId() + finalRound).incrementPointsThisRound(ergastStanding.getPoints());
                        });
                iterate = true;
            } else {
                iterate = false;
            }
            //dodaj bodove iz sprint utrke ako je bila
            response = ergastService.getSprintResultsByRound(properties.getCurrentSeasonPast(), round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                response.getMrData().getRaceTable().getRaces().get(0).getSprintResults()
                        .forEach(ergastStanding -> {
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId() + finalRound).incrementPointsThisRound(ergastStanding.getPoints());
                            constructorStandingByRound.get(ergastStanding.getConstructor().getConstructorId() + finalRound).incrementPointsThisRound(ergastStanding.getPoints());
                        });
            }
            round++;
        } while (iterate);
    }

    private void enrichSingleRoundStandingsWithRoundPoints(Map<String, DriverStandingByRound> driverStandingsByRound, Map<String, ConstructorStandingByRound> constructorStandingByRound) {
        ErgastResponse response = ergastService.getResultsByRound(properties.getCurrentSeasonPast(), CURRENT_ROUND);
        if (response.getMrData().getTotal() > 0) {
            Integer maxPosition = response.getMrData().getRaceTable().getRaces().get(0).getResults().size();
            response.getMrData().getRaceTable().getRaces().get(0).getResults()
                    .forEach(ergastStanding -> {
                        if (driverStandingsByRound != null) {
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId()).setDataFromARound(ergastStanding, maxPosition);
                        }
                        if (constructorStandingByRound != null) {
                            constructorStandingByRound.get(ergastStanding.getConstructor().getConstructorId()).incrementPointsThisRound(ergastStanding.getPoints());
                        }
                    });
        }
        //dodaj bodove iz sprint utrke ako je bila
        response = ergastService.getSprintResultsByRound(properties.getCurrentSeasonPast(), CURRENT_ROUND);
        if (response.getMrData().getTotal() > 0) {
            response.getMrData().getRaceTable().getRaces().get(0).getSprintResults()
                    .forEach(ergastStanding -> {
                        if (driverStandingsByRound != null) {
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId()).incrementPointsThisRound(ergastStanding.getPoints());
                        }
                        if (constructorStandingByRound != null) {
                            constructorStandingByRound.get(ergastStanding.getConstructor().getConstructorId()).incrementPointsThisRound(ergastStanding.getPoints());
                        }
                    });
        }
    }

    private Boolean refreshDriverStandingsFromErgast() {
        Boolean bool;
        List<DriverStanding> driverStandings = new ArrayList<>();
        Map<String, DriverStandingByRound> driverStandingsByRound = new HashMap<>();
        ErgastResponse response = ergastService.getCurrentDriverStandings();
        bool = checkSeasonAndRound(response.getMrData().getStandingsTable().getStandingsLists().get(0).getRound(),
                response.getMrData().getStandingsTable().getStandingsLists().get(0).getSeason());

        response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                .forEach(ergastStanding -> {
                    driverStandings.add(new DriverStanding(ergastStanding));
                    driverStandingsByRound.put(ergastStanding.getDriver().getDriverId(), new DriverStandingByRound(ergastStanding, properties.getCurrentSeasonPast(), CURRENT_ROUND));
                });
        response = ergastService.getResultsByRound(properties.getCurrentSeasonPast(), CURRENT_ROUND);
        response.getMrData().getRaceTable().getRaces().get(0).getResults()
                .forEach(ergastStanding -> {
                    driverStandingsByRound.get(ergastStanding.getDriver().getDriverId()).incrementPointsThisRound(ergastStanding.getPoints());
                });
        driverStandingsRepository.deleteAll();
        driverStandingsRepository.saveAll(driverStandings);
        enrichSingleRoundStandingsWithRoundPoints(driverStandingsByRound, null);
        driverStandingsByRoundRepository.saveAll(driverStandingsByRound.values());
        // updateExposureDriverList(driverStandings);
        return bool;
    }

    private Boolean checkSeasonAndRound(Integer round, Integer season) {
        Boolean response = false;

        if (season > properties.getCurrentSeasonPast()) {
            log.warn("UPDATED SEASON: {} - {} ", season, properties.getCurrentSeasonPast());
            properties.updateCurrentSeasonPast(season);
            CURRENT_ROUND = 1;
            propertiesRepository.updateProperty("round", CURRENT_ROUND.toString());
        } else if (CURRENT_ROUND < round) {
            CURRENT_ROUND = round;
            propertiesRepository.updateProperty("round", CURRENT_ROUND.toString());
        }
        log.info("checkSeasonAndRound: {}", response);
        return response;
    }


    private void initializeConstructorStandings() {
        List<ConstructorStanding> constructorStandings = new ArrayList<>();
        Map<String, ConstructorStandingByRound> constructorStandingsByRound = new HashMap<>();
        ErgastResponse response = ergastService.getConstructorStandings();
        response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                .forEach(ergastStanding -> {
                    constructorStandings.add(new ConstructorStanding(ergastStanding));
                    constructorStandingsByRound.put(ergastStanding.getConstructor().getConstructorId(), new ConstructorStandingByRound(ergastStanding, properties.getCurrentSeasonPast(), CURRENT_ROUND));
                });
        constructorStandingsRepository.deleteAll();
        constructorStandingsRepository.saveAll(constructorStandings);
        enrichSingleRoundStandingsWithRoundPoints(null, constructorStandingsByRound);
        constructorStandingsByRoundRepository.saveAll(constructorStandingsByRound.values());
    }

    @Override
    public Boolean fetchReplayLinks() {
        return videoService.fetchReplayLinks();
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
                return f1CommentRepository.updateStatus(id, 2);
            case 2:
                return newsCommentRepository.updateStatus(id, 6);
        }
        return -1;
    }

    @Override
    public NewsContent patchAwsPost(NewsContent entry) {
        return newsRepository.save(entry);
    }

    @Override
    public List<NewsContent> findNewsPosts(SearchParams searchParams) {
       log.info("mjesec je: {}", searchParams.getFromDate().getMonth());
        return newsRepository.findAllByTitleContainingIgnoreCase(searchParams.getSearchwords().get(0), searchParams.getFromDate());
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
    public Integer getNextRefreshTimeUsingCalendar(Integer seconds) {
        try {
            CalendarData calendarData = clientService.getCountdownData(0);
            log.info("getNextRefreshTimeUsingCalendar: " + calendarData.getF1Calendar().getSummary());
            Map<String, Integer> countdownData = calendarData.getCountdownData();
            if (countdownData.get("raceDays") > 2) {
                log.info("raceDays > 2");
                return null;
            }

            Integer fp1Seconds = countdownData.get("FP1Seconds");
            if (fp1Seconds != null && fp1Seconds > seconds) {
                return fp1Seconds - seconds;
            }
            Integer fp2Seconds = countdownData.get("FP2Seconds");
            if (fp2Seconds != null && fp2Seconds > seconds) {
                return fp2Seconds - seconds;
            }
            Integer fp3Seconds = countdownData.get("FP3Seconds");
            if (fp3Seconds != null && fp3Seconds > seconds) {
                return fp3Seconds - seconds;
            }
            Integer qualifyingSeconds = countdownData.get("qualifyingSeconds");
            if (qualifyingSeconds != null && qualifyingSeconds > seconds) {
                return qualifyingSeconds - seconds;
            }
            Integer raceSeconds = countdownData.get("raceSeconds");
            if (raceSeconds != null && raceSeconds > seconds) {
                return raceSeconds - seconds;
            }
        } catch (Exception e) {
            Logger.log("getNextRefreshTick error", e.getMessage());
            e.printStackTrace();
            return null;
        }
        log.info("getNextRefreshTimeUsingCalendar caught nothing");
        return null;
    }

    @Override
    public List<F1Comment> getAdminMessages() {
        return f1CommentRepository.findFirst30ByPageAndStatusOrderByTimestampDesc(47, 1);
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
    public List<Driver> getExposureDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public List<Driver> updateExposureDrivers(List<Driver> list) {
        driverRepository.deleteAll();
        driverRepository.saveAll(list);
        return driverRepository.findAll();
    }

    @Override
    public Aws backupPosts() {
        return Aws.builder()
                .newsContents(newsRepository.findAll())
                .newsComments(newsCommentRepository.findAll())
                .build();
    }

    @Override
    public Boolean restorePosts(Aws body) {
        newsRepository.saveAll(body.getNewsContents());
        newsCommentRepository.saveAll(body.getNewsComments());
        return true;
    }

    @Override
    public FullBackup fullBackup() {
        return FullBackup.builder()
                .artBackup(artService.getAllImages())
                .awsBackup(backupPosts())
                .exposureBackup(backupExposure())
                .marketingBackup(marketingService.backupMarketing()).build();
    }

    @Override
    public Boolean restoreFromFullBackup(FullBackup body) {
        artService.restoreAllImages(body.getArtBackup());
        restorePosts(body.getAwsBackup());
        restoreExposureFromBackup(body.getExposureBackup());
        marketingService.restoreMarketing(body.getMarketingBackup());
        return true;
    }

    @Override
    public Integer deleteAwsContent(String username) {
        return newsRepository.deleteAllByUsername(username);
    }

    @Override
    public String setOverlays(String overlays) {
        return clientService.setOverlays(overlays, false);
    }

    @Override
    public String setIframeLink(String link) {
        return clientService.setIframeLink(link);
    }

    @Override
    public Boolean generateChart() {
        generateChartsDriverStandingsByRound();
        generateChartsConstructorStandingsByRound();
        return true;
    }

    @Override
    public Boolean cleanup() throws Exception {
        instagramService.cleanup();
        twitterService.cleanup();

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.MONTH, -1);
        Date previousMonth = cal.getTime();
        newsRepository.deleteByTimestampCreatedBefore(previousMonth);

        return true;
    }

    @Override
    public Boolean instagramCleanup() throws Exception {
        return instagramService.cleanup();
    }

    @Override
    public String setInstagramWorkerPassword(String password) throws Exception {
        return instagramService.setInstagramWorkerPassword(password);
    }

    @Override
    public String setInstagramWorker(String username, String password) throws Exception {
        return instagramService.setInstagramWorker(username, password);
    }

    @Override
    public String turnOnOffInstagram(Boolean bool) throws Exception {
        return instagramService.turnOnOffInstagram(bool);
    }

    @Override
    public Boolean twitterCleanup() throws Exception {
        return twitterService.cleanup();
    }

    @Override
    public Boolean checkCurrentStream() throws IOException {
        return twitchService.checkCurrentStream();
    }

    @Override
    public Boolean fetchFourChanPosts() {
        fourchanService.fetch4chanPosts();
        return true;
    }

    @Override
    public Boolean deleteFourChanPosts() {
        fourchanService.deleteFourChanPosts();
        return true;
    }

    @Override
    public Boolean deleteFourChanPost(Integer id) {
        return fourchanService.deleteFourChanPost(id);
    }

    @Override
    public Boolean reverseGoogleImage(String url) {
        return fourchanService.reverseGoogleImage(url, true);
    }

    @Override
    public Boolean removeVideo(Integer id) {
        return videoService.removeVideo(id);
    }

    @Override
    public List<Replay> saveVideos(List<Replay> videos) {
        return videoService.saveVideos(videos);
    }

    @Override
    public String updateCurrentSeasonPast(Integer season) {
        return properties.updateCurrentSeasonPast(season);
    }

    @Override
    public String saveProperty(String name, String value) {
        properties.saveProperty(name, value);
        return "DONE";
    }

    @Override
    public F1Calendar getCalendar() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        return calendarRepository.findFirstByRaceAfterOrPractice3AfterOrderByPractice1(gmtDateTime, gmtDateTime);
    }

    @Override
    public F1Calendar saveCalendar(F1Calendar body) {
        calendarRepository.save(body);
        return body;
    }

    @Override
    public void updateOverlays(RaceAnalysis analysis) {
        log.info("updateOverlays -remove try/catch if everything ok");
        try {
            String newOverlays = "";
            Optional<sorim.f1.slasher.relentless.model.livetiming.Driver> opt = analysis.getDriverData().stream().filter(driver -> driver.getPosition() == 1).findFirst();
            if (opt.isPresent()) {
                String winnersName = opt.get().getName();
                String winnersTeamName = opt.get().getTeam();
                if (winnersName.equals("VERSTAPPEN")) {
                    newOverlays = "winner-verstappen";
                } else if (winnersName.equals("HAMILTON")) {
                    newOverlays = "winner-hamilton";
                } else if (winnersTeamName.equals("Ferrari")) {
                    newOverlays = "winner-ferrari";
                } else if (winnersTeamName.equals("Mercedes")) {
                    newOverlays = "winner-mercedes";
                }
            }
            if (newOverlays.equals("")) {
                List<sorim.f1.slasher.relentless.model.livetiming.Driver> ferraris = analysis.getDriverData().stream().filter(driver -> driver.getTeam().equals("Ferrari"))
                        .collect(Collectors.toList());
                if (!ferraris.isEmpty()) {
                    Boolean ferrariAboveP5 = ferraris.stream().anyMatch(driver -> driver.getPosition() <= 5);
                    if (!ferrariAboveP5) {
                        newOverlays = "loser-ferrari";
                    }
                }
            }
            log.info(newOverlays);
            clientService.setOverlays(newOverlays, true);
        } catch (Exception e) {
            log.error("updateOverlays error", e);
        }
    }

    @Override
    public JsonRepositoryModel updateJsonRepository(JsonRepositoryModel body) {
        jsonRepository.save(body);
        return jsonRepository.findAllById(body.getId());
    }

    @Override
    public JsonRepositoryModel getJsonRepository(String id) {
        return jsonRepository.findAllById(id);
    }

    @Override
    public JsonRepositoryTwoModel updateJsonRepositoryTwo(JsonRepositoryTwoModel body) {
        jsonRepositoryTwo.save(body);
        return jsonRepositoryTwo.findAllById(body.getId());
    }

    @Override
    public JsonRepositoryTwoModel getJsonRepositoryTwo(String id) {
        return jsonRepositoryTwo.findAllById(id);
    }

    public Boolean deleteJsonRepository(String id) {
        jsonRepository.deleteById(id);
        return true;
    }

    @Override
    public String postFormulaDankToInstagram() throws IGLoginException {
        return redditService.postFormulaDankToInstagram();
    }

    private void generateChartsDriverStandingsByRound() {
        List<DriverStandingByRound> standingsBySeason = driverStandingsByRoundRepository.findAllByIdSeasonOrderByIdRoundAscNameAsc(properties.getCurrentSeasonPast());
        List<DriverStanding> driverStandings = driverStandingsRepository.findAllByOrderByPositionAsc();
        Map<String, ChartSeries> totalPoints = new TreeMap<>();
        Map<String, ChartSeries> roundPoints = new TreeMap<>();
        List<ChartSeries> roundPointsSorted = new ArrayList<>();
        Map<String, ChartSeries> roundResults = new TreeMap<>();
        Map<String, ChartSeries> gridToResultChartIncludingDnf = new TreeMap<>();
        Map<String, ChartSeries> gridToResultChartWithoutDnf = new TreeMap<>();
        standingsBySeason.forEach(standing -> {
            if (!totalPoints.containsKey(standing.getCode())) {
                totalPoints.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundPoints.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundResults.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                gridToResultChartIncludingDnf.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>())
                        .series2(new ArrayList<>()).build());
                gridToResultChartWithoutDnf.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>())
                        .series2(new ArrayList<>()).build());
            }
            totalPoints.get(standing.getCode()).add(standing.getId().getRound(), standing.getPoints());
            roundPoints.get(standing.getCode()).add(standing.getId().getRound(), standing.getPointsThisRound());
            if (standing.getResultThisRound() != null) {
                if (isNumeric(standing.getResultThisRoundText())) {
                    roundResults.get(standing.getCode()).add(standing.getId().getRound(), new BigDecimal(standing.getResultThisRound()));
                } else {
                    roundResults.get(standing.getCode()).add(standing.getId().getRound(), new BigDecimal(0));

                }
            } else {
                roundResults.get(standing.getCode()).add(standing.getId().getRound(), null);
            }

            if (standing.getResultThisRound() != null) {
                gridToResultChartIncludingDnf.get(standing.getCode()).add2(standing.getGrid(), standing.getResultThisRoundDnf());
                if (isNumeric(standing.getResultThisRoundText())) {
                    gridToResultChartWithoutDnf.get(standing.getCode()).add2(standing.getGrid(), standing.getResultThisRoundDnf());
                }
            }
        });
        List<ChartSeries> gridToResultChartIncludingDnfList = new ArrayList<>(gridToResultChartIncludingDnf.values());
        List<ChartSeries> gridToResultChartWithoutDnfList = new ArrayList<>(gridToResultChartWithoutDnf.values());
        for (ChartSeries serie : gridToResultChartIncludingDnfList) {
            serie.calcSeries2Averages();
        }
        for (ChartSeries serie : gridToResultChartWithoutDnfList) {
            serie.calcSeries2Averages();
        }
        driverStandings.forEach(ds -> {
            if (roundPoints.containsKey(ds.getCode())) {
                roundPointsSorted.add(roundPoints.get(ds.getCode()));
            }
        });
        List<JsonRepositoryModel> output = new ArrayList<>();
        JsonRepositoryModel data1 = JsonRepositoryModel.builder().id("DRIVERS_TOTAL_POINTS")
                .json(new ArrayList<>(totalPoints.values())).build();
        JsonRepositoryModel data2 = JsonRepositoryModel.builder().id("DRIVERS_ROUND_POINTS")
                .json(new ArrayList<>(roundPointsSorted)).build();
        JsonRepositoryModel data3 = JsonRepositoryModel.builder().id("DRIVERS_ROUND_RESULTS")
                .json(new ArrayList<>(roundResults.values())).build();
        JsonRepositoryModel data4 = JsonRepositoryModel.builder().id("GRID_TO_RESULT_WITH_DNF")
                .json(gridToResultChartIncludingDnfList).build();
        JsonRepositoryModel data5 = JsonRepositoryModel.builder().id("GRID_TO_RESULT_WITHOUT_DNF")
                .json(gridToResultChartWithoutDnfList).build();
        output.add(data1);
        output.add(data2);
        output.add(data3);
        output.add(data4);
        output.add(data5);
        jsonRepository.saveAll(output);
    }

    private void generateChartsConstructorStandingsByRound() {
        List<ConstructorStandingByRound> standingsBySeason = constructorStandingsByRoundRepository.findAllByIdSeasonOrderByIdRoundAscNameAsc(properties.getCurrentSeasonPast());
        Map<String, ChartSeries> totalPoints = new TreeMap<>();
        Map<String, ChartSeries> roundPoints = new TreeMap<>();
        standingsBySeason.forEach(standing -> {
            if (!totalPoints.containsKey(standing.getId().getId())) {
                totalPoints.put(standing.getId().getId(), ChartSeries.builder()
                        .name(standing.getName())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundPoints.put(standing.getId().getId(), ChartSeries.builder()
                        .name(standing.getName())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
            }
            roundPoints.get(standing.getId().getId()).add(standing.getId().getRound(), standing.getPointsThisRound());
            totalPoints.get(standing.getId().getId()).add(standing.getId().getRound(), standing.getPoints());
        });

        List<JsonRepositoryModel> output = new ArrayList<>();
        JsonRepositoryModel data1 = JsonRepositoryModel.builder().id("CONSTRUCTOR_TOTAL_POINTS")
                .json(new ArrayList<>(totalPoints.values())).build();
        JsonRepositoryModel data2 = JsonRepositoryModel.builder().id("CONSTRUCTOR_ROUND_POINTS")
                .json(new ArrayList<>(roundPoints.values())).build();
        output.add(data1);
        output.add(data2);
        jsonRepository.saveAll(output);
    }

}
