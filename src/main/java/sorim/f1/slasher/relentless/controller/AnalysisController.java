package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.UpcomingRaceAnalysis;
import sorim.f1.slasher.relentless.service.LiveTimingService;
import sorim.f1.slasher.relentless.service.SecurityService;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("liveTiming")
public class AnalysisController {

    private final LiveTimingService service;
    private final SecurityService securityService;

    @GetMapping("/getAllRaceData/{year}")
    public boolean getAllRaceDataFromErgastTable(@RequestHeader String client, @PathVariable("year") String year, @RequestParam Boolean detailed) throws Exception {
        log.info("getAllRaceDataFromErgastTable: {}", year);
        securityService.validateAdminHeader(client);
        service.getAllRaceDataFromErgastTable(year, detailed);
        return true;
    }

    @GetMapping("/getRaceAnalysis")
    public RaceAnalysis getRaceAnalysis(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getRaceAnalysis();
    }

    @GetMapping("/getUpcomingRaceAnalysis")
    public UpcomingRaceAnalysis getUpcomingRaceAnalysis(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getUpcomingRaceAnalysis();
    }

    @GetMapping("/analyzeLatestRace")
    public Boolean analyzeLatestRace(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.analyzeLatestRace();
    }

    @GetMapping("/resetLatestRaceAnalysis")
    public Boolean resetLatestRaceAnalysis(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.resetLatestRaceAnalysis();
    }

    @GetMapping("/validateLatestRaceAnalysis")
    public String validateLatestRaceAnalysis(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.validateLatestRaceAnalysis();
    }


    @GetMapping("/upcomingRacesInitialLoad/{year}")
    public Boolean upcomingRacesInitialLoad(@RequestHeader String client, @PathVariable("year") String year) throws Exception {
        securityService.validateAdminHeader(client);
        return service.upcomingRacesAnalysisInitialLoad(year);
    }

    @GetMapping("/analyzeUpcomingRace")
    public Boolean analyzeUpcomingRace(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.analyzeUpcomingRace();
    }

    @GetMapping("/updateAllImageUrlsDev")
    public Boolean updateAllImageUrlsDev(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.updateAllImageUrlsDev();
    }

}
