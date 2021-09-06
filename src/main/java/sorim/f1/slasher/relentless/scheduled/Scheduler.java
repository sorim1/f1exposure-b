package sorim.f1.slasher.relentless.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.service.AdminService;
import sorim.f1.slasher.relentless.service.ClientService;
import sorim.f1.slasher.relentless.service.ExposureService;
import sorim.f1.slasher.relentless.service.LiveTimingService;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Scheduler {

    private final ExposureService exposureService;
    private final AdminService adminService;
    private final ClientService clientService;
    private final LiveTimingService liveTimingService;
    private final static String CODE="SCHEDULER";


    @Scheduled(cron = "0 0 10 * * MON")
    public void mondayJobs() {
        log.info("mondayJobs called");
        Logger.log(CODE, "mondayJobs called");
        adminService.deleteSportSurgeLinks();
        liveTimingService.analyzeLatestRace();
    }

    @Scheduled(cron = "0 0 10 * * TUE")
    public void tuesdayJobs() {
        log.info("tuesdayJobs called");
        Logger.log(CODE, "tuesdayJobs called");
        exposureService.closeExposurePoll();
    }

    @Scheduled(cron = "0 0 15 * * FRI")
    private void fridayJobs() throws IOException {
        log.info("fridayJobs called");
        fetchSportSurgeLinks();
        analyzeUpcomingRace();
    }

    // @Scheduled(fixedDelayString = "3600000", initialDelayString = "3600000")
    @Scheduled(cron = "0 0 10 * * SUN")
    public void sundayJobs(){
        log.info("sundayJobs called");
        exposureService.initializeExposure();

    }

    @Scheduled(cron = "0 0 15 * * SUN")
    private void sundayJobs2() throws IOException {
        int delay = 900000;
        log.info("sundayJobs2 - checkNewStandingsJob called");
        Boolean updated = adminService.initializeStandings();
        if(!updated){
            Logger.log(CODE, "checkNewStandingsJob delayed");
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            sundayJobs2();
                        }
                    },
                    delay
            );
        }
    }

    private void fetchSportSurgeLinks() throws IOException {
        Integer delay = adminService.fetchSportSurgeLinks();
        log.info("fetchSportSurgeLinks called");
        Logger.log(CODE, "fetchSportSurgeLinks called");
        if(delay!=null){
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            fetchSportSurgeLinks();
                        }
                    },
                    delay*1000
            );
        }
    }

    private void analyzeUpcomingRace() throws IOException {
        Integer delay = liveTimingService.analyzeUpcomingRace();
        log.info("fetchSportSurgeLinks called");
        Logger.log(CODE, "analyzeUpcomingRace called");
        if(delay!=null){
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            analyzeUpcomingRace();
                        }
                    },
                    delay*1000
            );
        }
    }
    @PostConstruct
    void onInit() throws IOException {
       log.info("onInitScheduler Called");
        sundayJobs();
        Integer weekDay = MainUtility.getWeekDay();
        try {
            switch (weekDay) {
                case 1: {
                    fridayJobs();
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
                case 4: {
                    break;
                }
                case 5: {
                    break;
                }
                case 6: {
                    fridayJobs();
                    break;
                }
                case 7: {
                    fridayJobs();
                    break;
                }
            }
        }catch(Exception e ){
            log.error(Arrays.toString(e.getStackTrace()));
            Logger.logProblem("onInitScheduler - " + Arrays.toString(e.getStackTrace()));
        }
    }

    @Scheduled(fixedRate=120*60*1000, initialDelay=60*1000)
    void bihourlyJob() throws Exception {
        log.info("bihourlyJob called");
        clientService.fetchInstagramFeed();
        clientService.fetchTwitterPosts();
    }
}
