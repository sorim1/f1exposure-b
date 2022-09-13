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

    private final ExposureStrawpollService exposureService;
    private final AdminService adminService;
    private final ClientService clientService;
    private final LiveTimingService liveTimingService;
    private final ErgastService ergastService;
    private final FourchanService fourchanService;
    private final InstagramService instagramService;

    private final MainProperties properties;
    private static final String CODE = "SCHEDULER";
    public static Boolean standingsUpdated = false;
    public static Boolean analysisDone = false;
    public static Boolean strawpollFound = false;
    private static boolean isRaceWeek = true;

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
        ergastService.fetchStatisticsFullFromPartial(false);
        isItRaceWeek();
    }

    @Scheduled(cron = "0 0 1 * * TUE")
    public void tuesdayJobs() throws IOException {
        log.info(CODE + " - tuesdayJobs called");
        if (!standingsUpdated) {
            adminService.initializeStandings(true);
            standingsUpdated = true;
        }
        isItRaceWeek();
        if (isRaceWeek) {
            clientService.setOverlays("", true);
        } else {
            clientService.setOverlays("sasha-sometimes", true);
        }
      //  fourchanService.cleanup();
    }

    @Scheduled(cron = "0 0 4 * * FRI")
    private void weekendJobsContinuous() throws IOException {
        log.info("fridayJobs called");
        isItRaceWeek();
        if (isRaceWeek) {
            analyzeUpcomingRacePeriodically();
            getImagesPeriodicallyRoot();
        }
    }

    private void isItRaceWeek() {
        CalendarData calendarData = clientService.getCountdownData(5);
        if(calendarData.getF1Calendar()!=null) {
            isRaceWeek = calendarData.getCountdownData().get("raceDays") < 6;
        } else {
            isRaceWeek = false;
        }
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
        if (!standingsUpdated && weekDay==1) {
            log.info(CODE + " - sundayStandingsJobs delayed");
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            imageFeedJob();
                            sundayStandingsJobs();
                        }
                    },
                    delay
            );
        }else {
            log.info(CODE + " - sundayStandingsJobs UPDATED");
        }
    }

    @Scheduled(cron = "0 0 8 * * SUN")
    private void sundayAnalysisJob() {
        if (isRaceWeek) {
            Integer delay;
            log.info(CODE + " - sundayAnalysisJob called");
            int weekDay = MainUtility.getWeekDay();
            if (!analysisDone && weekDay==1) {
                delay = liveTimingService.analyzeLatestRace(true);
                if (delay != null) {
                    int delayInMiliseconds = delay * 1000;
                    if (!strawpollFound) {
                        strawpollFound = exposureService.initializeExposureFrontendVariables(null);
                    }
                    log.info(CODE + " - sundayAnalysisJob delayed: " + delayInMiliseconds);
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @SneakyThrows
                                @Override
                                public void run() {
                                    sundayAnalysisJob();
                                    imageFeedJob();
                                }
                            },
                            delayInMiliseconds
                    );
                }
            }
        }
    }


    private void analyzeUpcomingRacePeriodically() {
        Integer delay = liveTimingService.analyzeUpcomingRace(false);
        log.info(CODE + " - analyzeUpcomingRacePeriodically called");
        int weekDay = MainUtility.getWeekDay();
        if (delay != null && weekDay>5) {
            int delayInMiliseconds = delay * 1000;
            MainUtility.logTime("analyzeUpcomingRacePeriodically", delayInMiliseconds);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
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
                            imageFeedJobWithoutInstagram();
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
        if(countdown>0) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            imageFeedJobWithoutInstagram();
                            getImagesPeriodically(countdown - 1);
                        }
                    },
                    delayInMiliseconds
            );
        } else {
            boolean isGenerating = liveTimingService.checkIfEventIsGenerating();
            if(isGenerating) {
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

    @PostConstruct
    void onInit() throws Exception {
        log.info("onInitScheduler Called");
        isItRaceWeek();
        int weekDay = MainUtility.getWeekDay();
     //   imageFeedJob();
     //   adminService.cleanup();
        try {
            switch (weekDay) {
                case 1:{
                    weekendJobsContinuous();
                    sundayAnalysisJob();
                    sundayStandingsJobs();
                    break;
                }
                case 6:
                case 7:{
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

    @Scheduled(cron = "0 0 1,6,10,12,14,16,18,20,22 * * *")
    void bihourlyJob() throws Exception {
        if(properties.getUrl().contains("f1exposure.com")){
            imageFeedJob();
            adminService.checkCurrentStream();
        } else {
            log.error("url not f1exposure.com");
            log.error(properties.getUrl());
        }

    }

     @Scheduled(cron = "0 0 11 * * *")
    void noonInstagramPost() throws Exception {
         Random rand = new Random();
         int minutes = rand.nextInt(30);
        log.info("noonInstagramPost called: " + minutes);
         if(properties.getUrl().contains("f1exposure.com")){
             Thread.sleep(1000 * 60 * minutes);
             fourchanService.postToInstagram(false);
             adminService.fetchFourChanPosts();
         //    instagramService.followMoreOnInstagram();
         } else {
             log.error("url not f1exposure.com");
             log.error(properties.getUrl());
         }
    }
   // @Scheduled(cron = "0 0 18 * * *")
    void eveningInstagramPost() throws Exception {
        Random rand = new Random();
        int minutes = rand.nextInt(30);
        log.info("eveningInstagramPost called: " + minutes);
        if(properties.getUrl().contains("f1exposure.com")){
            Thread.sleep(1000 * 60 * minutes);
            fourchanService.postToInstagram(true);
         //   instagramService.followMoreOnInstagram();
        } else {
            log.error("url not f1exposure.com");
            log.error(properties.getUrl());
        }
    }
}
