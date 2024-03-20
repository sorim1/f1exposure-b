package sorim.f1.slasher.relentless.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.model.CalendarData;
import sorim.f1.slasher.relentless.service.*;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Scheduler {

    private static final String CODE = "SCHEDULER";
    private static final String F1EXPOSURE_COM = "f1exposure.com";
    public static Boolean standingsUpdated = false;
    public static Boolean analysisDone = false;
    public static Boolean strawpollFound = false;
    private static boolean isRaceWeek = true;
    private final ExposureStrawpollService exposureService;
    private final AdminService adminService;
    private final ClientService clientService;
    private final LiveTimingService liveTimingService;
    private final ErgastService ergastService;
    private final StrawpollService strawpollService;
    private final RedditService redditService;
    private final MainProperties properties;

    @Scheduled(cron = "0 0 4 * * MON")
    public void mondayJobs() throws IOException {
        log.info(CODE + " mondayJobs called");
        exposureService.closeExposurePoll(true);
        analysisDone = true;
        strawpollFound = false;
        if (!standingsUpdated) {
            adminService.initializeStandings(true);
            standingsUpdated = true;
        }
        ergastService.fetchStatisticsFullFromPartial(true);
        isItRaceWeek();
    }

    @Scheduled(cron = "0 0 1 * * TUE")
    public void tuesdayJobs() throws IOException {
        log.info(CODE + " - tuesdayJobs called");
        if (!standingsUpdated) {
            log.info(CODE + " - standingsUpdating");
            adminService.initializeStandings(true);
            standingsUpdated = true;
        }
        if (isItRaceWeek()) {
            log.info(CODE + " - generating strawpoll");
            strawpollService.generateStrawpoll();
            log.info(CODE + " - generated strawpoll");
        }
        clientService.setOverlays("", true);
    }

    @Scheduled(cron = "0 0 1 * * WED")
    public void wednesdayJobs() {
        log.info(CODE + " - wednesdayJobs called");
        log.info(CODE + " - setNavbarData");
        clientService.setNavbarData();
    }

    @Scheduled(cron = "0 0 4 * * FRI")
    private void weekendJobsContinuous() throws IOException {
        log.info("fridayJobs called");
        if (isItRaceWeek()) {
            analyzeUpcomingRacePeriodically();
            getImagesPeriodicallyRoot();
        }
    }

    private Boolean isItRaceWeek() {
        CalendarData calendarData = clientService.getCountdownData(5);
        if (calendarData.getF1Calendar() != null) {
            isRaceWeek = calendarData.getCountdownData().get("raceDays") < 7;
        } else {
            isRaceWeek = false;
        }
        return isRaceWeek;
    }

    @Scheduled(cron = "0 0 4 * * SUN")
    public void sundayExposureJobs() {
        log.info("sundayJobs called");
        exposureService.initializeExposureFrontendVariables(null);

    }

    @Scheduled(cron = "0 0 15 * * SUN")
    private void sundayStandingsJobs() throws IOException {
        int delay = 1800000;
        log.info(CODE + " - sundayStandingsJobs called");
        adminService.initializeStandings(true);
        int weekDay = MainUtility.getWeekDay();
        if (!standingsUpdated && weekDay == 1) {
            log.info(CODE + " - sundayStandingsJobs delayed");
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            sundayStandingsJobs();
                        }
                    },
                    delay
            );
        } else {
            log.info(CODE + " - sundayStandingsJobs UPDATED");
        }
    }

    @Scheduled(cron = "0 0 4 * * SUN")
    private void sundayAnalysisJob() {
        if (isRaceWeek) {
            Integer delay;
            log.info(CODE + " - sundayAnalysisJob called");
            int weekDay = MainUtility.getWeekDay();
            if (!analysisDone || weekDay == 1) {
                delay = liveTimingService.analyzeLatestRace(true);
                log.info(CODE + " - sundayAnalysisJob delay: " + delay);
                if (delay != null) {
                    int delayInMiliseconds = delay * 1000;
                    int delayTillRaceStartInMiliseconds = (delay-6000) * 1000;
//                    if (!strawpollFound) {
//                        strawpollFound = exposureService.initializeExposureFrontendVariables(null);
//                    }
                    log.info(CODE + " - sundayAnalysisJob delayed: " + delayInMiliseconds);
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @SneakyThrows
                                @Override
                                public void run() {
                                    sundayAnalysisJob();
                                }
                            },
                            delayInMiliseconds
                    );
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @SneakyThrows
                                @Override
                                public void run() {
                                log.info(CODE + " - POCINJE LI UTRKA, PRIPREMI STRAWPOLL?: ");
                                    if(liveTimingService.checkIfRaceIsGenerating()){
                                        exposureService.raceHasStarted();
                                    }
                                }
                            },
                            delayTillRaceStartInMiliseconds
                    );

                }
            }
        }
    }


    private void analyzeUpcomingRacePeriodically() {
        Integer delay = liveTimingService.analyzeUpcomingRace(false);
        log.info(CODE + " - analyzeUpcomingRacePeriodically called");
        int weekDay = MainUtility.getWeekDay();
        if (delay != null && weekDay > 5) {
            int delayInMiliseconds = delay * 1000;
            MainUtility.logTime("analyzeUpcomingRacePeriodically", delayInMiliseconds);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            log.info(CODE + " - analyzeUpcomingRace 1: " + delay);
                            imageFeedJobWithoutInstagram();
                            analyzeUpcomingRacePeriodically();
                        }
                    },
                    delayInMiliseconds
            );
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            log.info(CODE + " - analyzeUpcomingRace 2: " + delay);
                            liveTimingService.analyzeUpcomingRace(false);
                        }
                    },
                    delayInMiliseconds + 1500000L
            );
        }
    }

    private void getImagesPeriodicallyRoot() {
        Integer delay = adminService.getNextRefreshTimeUsingCalendar(3600);
        log.info(CODE + " - getImagesPeriodicallyRoot called: " + delay);
        if (delay != null) {
            int delayInMiliseconds = delay * 1000;
            MainUtility.logTime("getImagesPeriodicallyRoot", delayInMiliseconds);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            imageFeedJobWithoutInstagram();
                            getImagesPeriodically(9);
                        }
                    },
                    delayInMiliseconds
            );
        }
    }

    private void getImagesPeriodically(Integer countdown) {
        log.info(CODE + " - getImagesPeriodically called " + countdown);
        int delayInMiliseconds = 900000;
        if (countdown > 0) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            if (countdown == 2) {
                                imageFeedJob();
                            } else {
                                imageFeedJobWithoutInstagram();
                            }
                            getImagesPeriodically(countdown - 1);
                        }
                    },
                    delayInMiliseconds
            );
        } else {
            boolean isGenerating = liveTimingService.checkIfEventIsGenerating();
            if (isGenerating) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @SneakyThrows
                            @Override
                            public void run() {
                                imageFeedJobWithoutInstagram();
                                getImagesPeriodically(0);
                            }
                        },
                        delayInMiliseconds
                );
            } else {
                getImagesPeriodicallyRoot();
                liveTimingService.analyzeUpcomingRace(false);
                liveTimingService.analyzeLatestRace(true);
            }
        }
    }

  //  @PostConstruct
    void onInit() throws Exception {
        log.info("onInitScheduler Called");
        isItRaceWeek();
        int weekDay = MainUtility.getWeekDay();
        //   adminService.cleanup();
        try {
            switch (weekDay) {
                case 1: {
                    weekendJobsContinuous();
                    sundayAnalysisJob();
                    sundayStandingsJobs();
                    break;
                }
                case 6:
                case 7: {
                    weekendJobsContinuous();
                    break;
                }
                case 2: {
                    mondayJobs();
                    break;
                }
                case 3: {
                    tuesdayJobs();
                    break;
                }
                case 4:
                case 5: {
                    break;
                }
            }
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    void imageFeedJob() throws Exception {
        log.info("imageFeedJob called");
        clientService.fetchImageFeed();
    }

    void imageFeedJobWithoutInstagram() throws Exception {
        log.info("imageFeedJobWithoutInstgram called");
          clientService.fetchTwitterPosts();
        clientService.fetchRedditPosts();
    }

   // @Scheduled(cron = "0 0 1,8,10,12,14,16,18,20,22 * * *")
    @Scheduled(cron = "0 0 7,11,15,20 * * *")
    void bihourlyJob() throws Exception {
        if (properties.getUrl().contains(F1EXPOSURE_COM)) {
            imageFeedJobWithoutInstagram();
          //  adminService.checkCurrentStream();
        } else {
            log.error("url not " + F1EXPOSURE_COM);
            log.error(properties.getUrl());
        }

    }

    @Scheduled(cron = "0 0 12 * * *")
    void firstInstagramJob() throws Exception {
        Random rand = new Random();
        int minutes = rand.nextInt(20);
        log.info("firstInstagramJob to be called: " + minutes);
        if (properties.getUrl().contains(F1EXPOSURE_COM)) {
            Thread.sleep(1000 * 60 * minutes);
            try {
                clientService.fetchInstagramPosts();
            } catch (Exception e) {
                log.info("firstInstagramJob fetch failed: {}", e.getMessage());
            }
            Thread.sleep(1000 * 60 * minutes);
            String title = redditService.postFormulaDankToInstagram();
            log.info("firstInstagramJob ended: " + title);
        } else {
            log.warn("url not " + F1EXPOSURE_COM);
            log.warn(properties.getUrl());
        }
    }

    // @Scheduled(cron = "0 0 1 * * *")
    void secondInstagramJob() throws Exception {
        Random rand = new Random();
        int minutes = rand.nextInt(20);
        log.info("secondInstagramJob called: " + minutes);
        if (properties.getUrl().contains(F1EXPOSURE_COM)) {
            Thread.sleep(1000 * 60 * minutes);
            try {
                clientService.fetchInstagramPosts();
            } catch (Exception e) {
                log.info("secondInstagramJob fetch failed: {}", e.getMessage());
            }
            Thread.sleep(1000 * 60 * minutes);
            String title = redditService.postFormulaDankToInstagram();
            log.info("secondInstagramJob ended: " + title);
        } else {
            log.warn("url not " + F1EXPOSURE_COM);
            log.warn(properties.getUrl());
        }
    }
}
