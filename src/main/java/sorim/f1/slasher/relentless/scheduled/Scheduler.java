package sorim.f1.slasher.relentless.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.service.AdminService;
import sorim.f1.slasher.relentless.service.ClientService;
import sorim.f1.slasher.relentless.service.ExposureService;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Scheduler {

    private final ExposureService exposureService;
    private final AdminService adminService;
    private final ClientService clientService;

   // @Scheduled(fixedDelayString = "3600000", initialDelayString = "3600000")
   @Scheduled(cron = "0 0 10 * * SUN")
    public void sundayJobs(){
        log.info("sundayJobs called");
       exposureService.setExposureStartTimeOnASunday();

    }

    @Scheduled(cron = "0 0 15 * * SUN")
    private void checkNewStandingsJob() throws IOException {
       int delay = 900000;
        log.info("checkNewStandingsJob called");
        Boolean updated = adminService.initializeStandings();
        if(!updated){
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            checkNewStandingsJob();
                        }
                    },
                    delay
            );
        }
    }

    @Scheduled(cron = "0 0 10 * * MON")
    public void mondayJobs() {
        log.info("mondayJobs called");
        adminService.deleteSportSurgeLinks();
    }

    @Scheduled(cron = "0 0 10 * * TUE")
    public void tuesdayJobs() {
        log.info("tuesdayJobs called");
        exposureService.closeExposurePoll();
    }

    @Scheduled(cron = "0 0 15 * * FRI")
    private void fridayJobs() throws IOException {
        log.info("fridayJobs called");
        fetchSportSurgeLinks();
    }

    private void fetchSportSurgeLinks() throws IOException {
        Integer delay = adminService.fetchSportSurgeLinks();
        log.info("fetchSportSurgeLinksSCh called");
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
    @PostConstruct
    void onInit(){
       log.info("onInitCalled");
        sundayJobs();
    }

    //@Scheduled(fixedRate=120*60*1000, initialDelay=60*1000)
    void bihourlyJob() throws Exception {
        log.info("bihourlyJob called");
        clientService.fetchInstagramFeed();
        clientService.fetchTwitterPosts();
    }
}
