package sorim.f1.slasher.relentless.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.service.*;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
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
    private final static String CODE="SCHEDULER";
    public static Boolean standingsUpdated=false;
    public static Boolean analysisDone=false;
    public static Boolean strawpollFound=false;

    @Scheduled(cron = "0 0 1 * * MON")
    public void mondayJobs() throws IOException {
        Logger.log(CODE, "mondayJobs called");
        adminService.deleteSportSurgeLinks();
        adminService.fetchReplayLinks();
        exposureService.closeExposurePoll();
        analysisDone=true;
        strawpollFound=false;
        if(!standingsUpdated) {
            standingsUpdated = adminService.initializeStandings();
        }
        Boolean artGenerated = artService.generateLatestArt();
        Logger.log(CODE, "artGenerated: " + artGenerated);

    }

    @Scheduled(cron = "0 0 18 * * TUE")
    public void tuesdayJobs() throws IOException {
        Logger.log(CODE, "tuesdayJobs called");
        adminService.fetchReplayLinks();
        if(!standingsUpdated) {
            adminService.initializeStandings();
            standingsUpdated=true;
        }
    }

    @Scheduled(cron = "0 0 4 * * FRI")
    private void weekendJobsContinuous() throws IOException {
        log.info("fridayJobs called");
        fetchSportSurgeLinksPeriodically();
        analyzeUpcomingRacePeriodically();
    }

    @Scheduled(cron = "0 0 4 * * SUN")
    public void sundayExposureJobs(){
        log.info("sundayJobs called");
        exposureService.initializeExposureFrontendVariables(null);

    }

    @Scheduled(cron = "0 0 15 * * SUN")
    private void sundayStandingsJobs() throws IOException {
        int delay = 1800000;
        Logger.log(CODE, "sundayStandingsJobs called");
        adminService.initializeStandings();
        if(!standingsUpdated){
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
        }
    }

    @Scheduled(cron = "0 0 8 * * SUN")
    private void sundayAnalysisJob(){
        int delay = 1800000;
        Logger.log(CODE, "sundayAnalysisJob called");
        liveTimingService.analyzeLatestRace();
        if(!strawpollFound) {
            strawpollFound = exposureService.initializeExposureFrontendVariables(null);
        }
        if(!analysisDone){
            Logger.log(CODE, "sundayAnalysisJob delayed");
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            sundayAnalysisJob();
                        }
                    },
                    delay
            );
        }
    }


    private void fetchSportSurgeLinksPeriodically() throws IOException {
        Integer delay = adminService.fetchSportSurgeLinks();
        Logger.log(CODE, "fetchSportSurgeLinksPeriodically called");
        if(delay!=null){
            int delayInMiliseconds=delay*1000;
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

    private void analyzeUpcomingRacePeriodically(){
        Integer delay = liveTimingService.analyzeUpcomingRace();
        Logger.log(CODE, "analyzeUpcomingRacePeriodically called");
        if(delay!=null){
            int delayInMiliseconds=delay*1000;
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
            //also do it hour later
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            liveTimingService.analyzeUpcomingRace();
                        }
                    },
                    delayInMiliseconds+3600000
            );
        }
    }
    @PostConstruct
    void onInit(){
       log.info("onInitScheduler Called");
        sundayExposureJobs();
        int weekDay = MainUtility.getWeekDay();
        try {
            switch (weekDay) {
                case 1:
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
        }catch(Exception e ){
            log.error(Arrays.toString(e.getStackTrace()));
            }
    }

    @Scheduled(cron = "0 0 1,6,8,10,12,14,16,18,20,22 * * *")
    void imageFeedJob() throws Exception {
        log.info("bihourlyJob called");
        clientService.fetchInstagramFeed();
        clientService.fetchTwitterPosts();
        clientService.fetchRedditPosts();
    }
}
