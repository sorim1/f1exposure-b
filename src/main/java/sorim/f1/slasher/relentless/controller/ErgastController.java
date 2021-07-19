package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.ergast.Race;
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
    List<Race> fetchCurrentSeason(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.fetchCurrentSeason();
    }

    @GetMapping("/fetchSeason")
    List<Race> fetchSeason(@RequestHeader String client, @PathVariable String year) throws Exception {
        securityService.validateAdminHeader(client);
        log.info("year: {}", year);
        return service.fetchSeason(year);
    }
}
