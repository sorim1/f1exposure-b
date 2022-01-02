package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.AwsContent;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.DriverComparator;
import sorim.f1.slasher.relentless.model.DriverCompared;
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
    public List<RaceData> fetchCurrentSeason(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.fetchCurrentSeason();
    }

    @GetMapping("/fetchSeason/{year}")
    public List<RaceData> fetchSeason(@RequestHeader String client, @PathVariable String year) throws Exception {
        securityService.validateAdminHeader(client);
        log.info("year: {}", year);
        return service.fetchSeason(year);
    }

    @GetMapping("/fetchHistoricSeasonFull")
    public Boolean fetchHistoricSeasonFull(@RequestHeader String client) throws Exception {
         securityService.validateAdminHeader(client);
        log.info("fetchHistoricSeasonFull");
        return service.fetchHistoricSeasonFull();
    }

    @GetMapping("/fetchStatistics")
    public Boolean fetchStatistics(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        log.info("fetchStatistics");
        return service.fetchStatistics(false);
    }

    @GetMapping("/fetchStatisticsPartial")
    public Boolean fetchStatisticsPartial(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        log.info("fetchStatisticsPartial");
        return service.fetchStatistics(true);
    }

    @GetMapping("/fetchStatisticsFullFromPartial")
    public Boolean fetchStatisticsFullFromPartial(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        log.info("fetchStatisticsFullFromPartial");
        return service.fetchStatisticsFullFromPartial();
    }

    @GetMapping("/generateAllErgastDrivers")
    public List<ErgastDriver> generateAllErgastDrivers(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        log.info("generateAllErgastDrivers");
        return service.generateAllErgastDrivers();
    }

    @GetMapping("/getErgastDrivers")
    public Object getErgastDrivers(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getErgastDrivers();
    }

    @GetMapping("/getDriverStatistics/{driverId}")
    Object getDriverStatistics(@RequestHeader String client, @PathVariable String driverId) throws Exception {
        securityService.validateHeader(client);
        return service.getDriverStatistics(driverId);
    }

    @GetMapping("/getErgastConstructors")
    public Object getErgastConstructors(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getErgastConstructors();
    }

    @GetMapping("/getConstructorStatistics/{constructorId}")
    public Object getConstructorStatistics(@RequestHeader String client, @PathVariable String constructorId) throws Exception {
        securityService.validateHeader(client);
        return service.getConstructorStatistics(constructorId);
    }

    @GetMapping("/getHistoricSeason/{season}")
    public Object getDriverStatistics(@RequestHeader String client, @PathVariable Integer season) throws Exception {
        securityService.validateHeader(client);
        return service.getHistoricSeason(season);
    }



    @GetMapping("/getCircuitStatistics/{circuitId}")
    public Object getCircuitStatistics(@RequestHeader String client, @PathVariable String circuitId) throws Exception {
        securityService.validateHeader(client);
        return service.getCircuitStatistics(circuitId);
    }

    @GetMapping("/getAllCircuits")
    public Object getAllCircuits(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getAllCircuits();
    }

    @PostMapping("/compareDrivers")
    public DriverComparator compareDrivers(@RequestHeader String client, @RequestBody DriverComparator body) throws Exception {
        securityService.validateHeader(client);
        log.info("compareDrivers");
        return service.compareDrivers(body);
    }

    @GetMapping("/compareDriversDropdown/{season}")
    public List<DriverCompared> getCompareDriversDropdown(@RequestHeader String client, @PathVariable Integer season) throws Exception {
        securityService.validateHeader(client);
        log.info("compareDrivers");
        return service.getCompareDriversDropdown(season);
    }


    @GetMapping("/getErgastRace/{season}/{round}")
    public Object getErgastRace(@RequestHeader String client, @PathVariable Integer season, @PathVariable Integer round) throws Exception {
        securityService.validateHeader(client);
        return service.getErgastRace(season, round);
    }
}
