package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.F1Calendar;
import sorim.f1.slasher.relentless.service.AdminService;
import sorim.f1.slasher.relentless.service.SecurityService;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("sorimzone")
public class AdminController {

    private final AdminService service;
    private final SecurityService securityService;

    @GetMapping("/refreshCalendar")
    boolean refreshCalendar(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.refreshCalendarOfCurrentSeason();
        return true;
    }

    @GetMapping("/upcomingRaceCalendar")
    F1Calendar getUpcomingRace(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.getUpcomingRace();
    }

    @PatchMapping("/upcomingRaceCalendar")
    F1Calendar updateUpcomingRace(@RequestHeader String client, @RequestBody F1Calendar entry) throws Exception {
        securityService.validateAdminHeader(client);
        return service.updateUpcomingRace(entry);
    }

    @GetMapping("/validateCalendarForNextRace")
    boolean validateCalendarForNextRace(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.validateCalendarForNextRace();
        return true;
    }


    @GetMapping("/initialize")
    boolean initialize(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.initialize();
        return true;
    }

    @GetMapping("/initializeStandings")
    Boolean intializeStandings(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.initializeStandings();
    }

    @GetMapping("/initializeFullStandingsThroughRounds")
    Boolean initializeFullStandingsThroughRounds(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.initializeFullStandingsThroughRounds();
    }

    @GetMapping("/fetchSportSurgeLinks")
    void fetchSportSurgeLinks(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.fetchSportSurgeLinks();
    }

    @GetMapping("/closeExposurePoll")
    void closeExposurePoll(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.closeExposurePoll();
    }

}
