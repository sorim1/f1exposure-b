package sorim.f1.slasher.relentless.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.service.ExposureService;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Scheduler {

    private final ExposureService exposureService;

   // @Scheduled(fixedDelayString = "3600000", initialDelayString = "3600000")
   @Scheduled(cron = "0 0 10 * * SUN")
    public void sundayJobs(){
        log.info("sundayJobs called");
       exposureService.setExposureStartTime();
    }

    @Scheduled(cron = "0 0 10 * * MON")
    public void mondayJobs(){
        log.info("mondayJobs called");
        exposureService.setExposureCloseTime();
    }


}
