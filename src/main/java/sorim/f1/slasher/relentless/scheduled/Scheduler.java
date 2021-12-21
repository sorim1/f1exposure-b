package sorim.f1.slasher.relentless.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.CalendarData;
import sorim.f1.slasher.relentless.service.*;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Scheduler {

    private final ExposureStrawpollService exposureService;
    private final AdminService adminService;
    private final ClientService clientService;
    private final ArtService artService;
    private final LiveTimingService liveTimingService;
    private final static String CODE = "SCHEDULER";
    public static Boolean standingsUpdated = false;
    public static Boolean analysisDone = false;
    public static Boolean strawpollFound = false;
    public static Boolean isRaceWeek = true;

    @Scheduled(cron = "0 0 1 * * MON")
    public void mondayJobs() throws IOException {
        Logger.log(CODE, "mondayJobs called");
        adminService.deleteSportSurgeLinks();
        adminService.fetchReplayLinks();
        exposureService.closeExposurePoll();
        analysisDone = true;
        strawpollFound = false;
        if (!standingsUpdated) {
            adminService.initializeStandings();
            standingsUpdated = true;
        }
        Boolean artGenerated = artService.generateLatestArt();
        Logger.log(CODE, "artGenerated: " + artGenerated);
        isItRaceWeek();
    }

    @Scheduled(cron = "0 0 18 * * TUE")
    public void tuesdayJobs() throws IOException {
        Logger.log(CODE, "tuesdayJobs called");
        adminService.fetchReplayLinks();
        if (!standingsUpdated) {
            adminService.initializeStandings();
            standingsUpdated = true;
        }
    }

    @Scheduled(cron = "0 0 4 * * FRI")
    private void weekendJobsContinuous() throws IOException {
        log.info("fridayJobs called");
        isItRaceWeek();
        if (isRaceWeek) {
            analyzeUpcomingRacePeriodically();
        }
    }

    private void isItRaceWeek() {
        CalendarData calendarData = clientService.getCountdownData(5);
        if(calendarData.getF1Calendar()!=null) {
            isRaceWeek = calendarData.getCountdownData().get("raceDays") < 5;
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
        Logger.log(CODE, "sundayStandingsJobs called");
        adminService.initializeStandings();
        if (!standingsUpdated) {
            Logger.log(CODE, "sundayStandingsJobs delayed");
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
        }else {
            Logger.log(CODE, "sundayStandingsJobs UPDATED");
        }
    }

    @Scheduled(cron = "0 0 8 * * SUN")
    private void sundayAnalysisJob() {
        if (isRaceWeek) {
            Integer delay;
            Logger.log(CODE, "sundayAnalysisJob called");
            if (!analysisDone) {
                delay = liveTimingService.analyzeLatestRace();
                if (delay != null) {
                    int delayInMiliseconds = delay * 1000;
                    if (!strawpollFound) {
                        strawpollFound = exposureService.initializeExposureFrontendVariables(null);
                    }
                    Logger.log(CODE, "sundayAnalysisJob delayed: " + delayInMiliseconds);
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
                }
            }
        }
    }


    private void fetchSportSurgeLinksPeriodically() throws IOException {
        Integer delay = adminService.fetchSportSurgeLinks();
        Logger.log(CODE, "fetchSportSurgeLinksPeriodically called");
        if (delay != null) {
            int delayInMiliseconds = delay * 1000;
            MainUtility.logTime("fetchSportSurgeLinksPeriodically", delayInMiliseconds);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            fetchSportSurgeLinksPeriodically();
                        }
                    },
                    delayInMiliseconds
            );

        }
    }

    private void analyzeUpcomingRacePeriodically() {
        Integer delay = liveTimingService.analyzeUpcomingRace(false);
        Logger.log(CODE, "analyzeUpcomingRacePeriodically called");
        if (delay != null) {
            int delayInMiliseconds = delay * 1000;
            MainUtility.logTime("analyzeUpcomingRacePeriodically", delayInMiliseconds);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
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
                            liveTimingService.analyzeUpcomingRace(false);
                        }
                    },
                    delayInMiliseconds + 1500000
            );
        }
    }


    //@PostConstruct
    void onInit() {
        log.info("onInitScheduler Called");
        //sundayExposureJobs();
        isItRaceWeek();
        int weekDay = MainUtility.getWeekDay();
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

    //@Scheduled(cron = "0 0 1,6,8,10,12,14,16,18,20,22 * * *")
    void imageFeedJob() throws Exception {
        log.info("bihourlyJob called");
        clientService.fetchInstagramFeed();
        clientService.fetchTwitterPosts();
        clientService.fetchRedditPosts();
    }
}
