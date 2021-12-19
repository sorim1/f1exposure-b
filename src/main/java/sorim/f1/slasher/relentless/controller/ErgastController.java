package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.AllStandings;
import sorim.f1.slasher.relentless.model.ergast.ErgastDriver;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("ergast")
public class ErgastController {

    private final ErgastService service;
    private final SecurityService securityService;

    @GetMapping("/fetchCurrentSeason")
    List<RaceData> fetchCurrentSeason(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.fetchCurrentSeason();
    }

    @GetMapping("/fetchSeason/{year}")
    List<RaceData> fetchSeason(@RequestHeader String client, @PathVariable String year) throws Exception {
        securityService.validateAdminHeader(client);
        log.info("year: {}", year);
        return service.fetchSeason(year);
    }

    @GetMapping("/fetchHistoricSeason/{year}")
    AllStandings fetchHistoricSeason(@RequestHeader String client, @PathVariable Integer year) throws Exception {
       // securityService.validateAdminHeader(client);
        log.info("fetchHistoricSeason: {}", year);
        return service.fetchHistoricSeason(year);
    }

    @GetMapping("/fetchHistoricSeasonFull")
    Boolean fetchHistoricSeasonFull(@RequestHeader String client) throws Exception {
         securityService.validateAdminHeader(client);
        log.info("fetchHistoricSeasonFull");
        return service.fetchHistoricSeasonFull();
    }

    @GetMapping("/getHistoricSeason/{year}")
    Object getHistoricSeason(@RequestHeader String client, @PathVariable Integer year) throws Exception {
         securityService.validateHeader(client);
        return service.getHistoricSeason(year);
    }

    @GetMapping("/fetchDriverStatistics")
    Boolean fetchDriverStatistics(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        log.info("fetchDriverStatistics");
        return service.fetchDriverStatistics();
    }

    @GetMapping("/generateAllErgastDrivers")
    List<ErgastDriver> generateAllErgastDrivers(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        log.info("fetchDriverStatistics");
        return service.generateAllErgastDrivers();
    }

    @GetMapping("/getErgastDrivers")
    Object getErgastDrivers(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getErgastDrivers();
    }

    @GetMapping("/getDriverStatistics/{driverId}")
    Object getDriverStatistics(@RequestHeader String client, @PathVariable String driverId) throws Exception {
        securityService.validateHeader(client);
        return service.getDriverStatistics(driverId);
    }
}
