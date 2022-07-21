package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.SessionInfo;
import sorim.f1.slasher.relentless.model.livetiming.UpcomingRaceAnalysis;
import sorim.f1.slasher.relentless.service.LiveTimingRadioService;
import sorim.f1.slasher.relentless.service.LiveTimingService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("liveTiming")
public class LiveTimingController {

    private final LiveTimingService service;
    private final LiveTimingRadioService liveTimingRadioService;
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

    @GetMapping("/getUpcomingRace")
    public RaceData getUpcomingRace(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getUpcomingRace();
    }

    @GetMapping("/analyzeLatestRace")
    public Integer analyzeLatestRace(@RequestHeader String client, @RequestParam(value = "iterate", required = false) Integer iterate) throws Exception {
        securityService.validateAdminHeader(client);
        if (iterate != null) {
            for (int i = 0; i < iterate; i++) {
                service.analyzeLatestRace(false);
            }
            return iterate;
        } else {
            return service.analyzeLatestRace(true);
        }
    }

    @GetMapping("/analyzeRace")
    public Boolean analyzeLatestRace(@RequestHeader String client, @RequestParam Integer season, @RequestParam Integer round) throws Exception {
        securityService.validateAdminHeader(client);
        return service.analyzeRace(season, round);
    }

    @GetMapping("/resetLatestRaceAnalysis")
    public Boolean resetLatestRaceAnalysis(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.resetLatestRaceAnalysis();
    }

    @GetMapping("/deleteLatestRaceAnalysis")
    public Boolean deleteLatestRaceAnalysis(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.deleteLatestRaceAnalysis();
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
    public Integer analyzeUpcomingRace(@RequestHeader String client, @RequestParam(value = "redo", required = false) Boolean redo) throws Exception {
        securityService.validateAdminHeader(client);
        return service.analyzeUpcomingRace(redo);
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


    @PatchMapping("/updateCircuitImage/{season}/{round}")
    public String updateCircuitImage(@RequestHeader String client, @PathVariable("season") String season, @PathVariable("round") Integer round, @RequestBody String newImageUrl) throws Exception {
        securityService.validateAdminHeader(client);
        return service.updateCircuitImage(season, round, newImageUrl);
    }

    @GetMapping("/setLatestTreeMap")
    public Boolean setLatestTreeMap(@RequestHeader String client, @RequestParam(value = "ergastStandingsUpdated", required = false) Boolean ergastStandingsUpdated) throws Exception {
        return service.setLatestTreeMap(ergastStandingsUpdated);
    }

    @GetMapping("/backupRaceData/{id}")
    public RaceData backupRaceData(@RequestHeader String client, @PathVariable("id") Integer id) throws Exception {
        securityService.validateAdminHeader(client);
        return service.backupRaceData(id);
    }

    @PostMapping("/restoreRaceData/{id}")
    public RaceData restoreRaceData(@RequestHeader String client, @PathVariable("id") Integer id, @RequestBody RaceData body) throws Exception {
        securityService.validateAdminHeader(client);
        return service.restoreRaceData(id, body);
    }

    @GetMapping("/getSessionInfo")
    public SessionInfo getSessionInfo(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.getSessionInfo();
    }

    @GetMapping("/generatePostRaceRadio")
    public String generatePostRaceRadio() throws Exception {
        return liveTimingRadioService.generatePostRaceRadio();
    }
}
