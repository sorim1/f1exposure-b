package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.UpcomingRaceAnalysis;
import sorim.f1.slasher.relentless.service.LiveTimingService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("liveTiming")
public class AnalysisController {

    private final LiveTimingService service;
    private final SecurityService securityService;

    @GetMapping("/getAllRaceData/{year}")
    public boolean getAllRaceDataFromErgastTable(@RequestHeader String client, @PathVariable("year") String year, @RequestParam Boolean detailed, @RequestParam Boolean deleteOld) throws Exception {
        log.info("getAllRaceDataFromErgastTable: {}", year);
        securityService.validateAdminHeader(client);
        service.getAllRaceDataFromErgastTable(year, detailed, deleteOld);
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
    public Integer analyzeUpcomingRace(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.analyzeUpcomingRace();
    }

    @GetMapping("/updateAllImageUrlsDev")
    public Boolean updateAllImageUrlsDev(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.updateAllImageUrlsDev();
    }

    @GetMapping("/findRacesBySeason/{year}")
    public List<RaceData> findRacesBySeason(@RequestHeader String client, @PathVariable("year") String year) throws Exception {
        securityService.validateAdminHeader(client);
        return service.findRacesBySeason(year);
    }



    @GetMapping("/deleteRacesBySeason/{year}")
    public Boolean deleteRacesBySeason(@RequestHeader String client, @PathVariable("year") String year) throws Exception {
        securityService.validateAdminHeader(client);
        return service.deleteRacesBySeason(year);
    }

}
