package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.Aws;
import sorim.f1.slasher.relentless.model.FullBackup;
import sorim.f1.slasher.relentless.model.FullExposure;
import sorim.f1.slasher.relentless.service.AdminService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.util.List;

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

    @GetMapping("/setCountdownMode/{mode}")
    String setCountdownMode(@RequestHeader String client,@PathVariable("mode") String mode) throws Exception {
        securityService.validateAdminHeader(client);
        return service.setCountdownMode(mode);
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

    @GetMapping("/fetchReplayLinks")
    void fetchReplayLinks(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.fetchReplayLinks();
    }

    @GetMapping("/deleteComment/{mode}/{id}")
    Integer deleteComment(@RequestHeader String client, @PathVariable("mode") String mode, @PathVariable("id") String id) throws Exception {
        securityService.validateAdminHeader(client);
        return service.deleteComment(Integer.valueOf(mode),Integer.valueOf(id));
    }

    @PatchMapping("/updateAwsPost/")
    AwsContent getAwsPost(@RequestHeader String client, @RequestBody AwsContent entry) throws Exception {
        securityService.validateHeader(client);
        return service.patchAwsPost(entry);
    }


    @GetMapping("/updateCurrentRound")
    List<Integer> updateCurrentRound(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.updateCurrentRound(false);
    }

    @GetMapping("/updateCurrentRoundUp")
    List<Integer> updateCurrentRoundUp(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.updateCurrentRound(true);
    }

    @GetMapping("/updateCurrentRound/{round}")
    List<Integer> setCurrentRound(@RequestHeader String client, @PathVariable("round") String round) throws Exception {
        securityService.validateAdminHeader(client);
        return service.setCurrentRound(Integer.valueOf(round));
    }

    @GetMapping("/backupExposure")
    FullExposure getExposureDriverList(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.backupExposure();
    }

    @PostMapping("/restoreExposureFromBackup")
    Boolean restoreExposureFromBackup(@RequestHeader String client, @RequestBody FullExposure body) throws Exception {
        securityService.validateAdminHeader(client);
        return service.restoreExposureFromBackup(body);
    }

    @GetMapping("/fullBackup")
    FullBackup fullBackup(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.fullBackup();
    }

    @PostMapping("/fullBackup")
    Boolean fullBackup(@RequestHeader String client, @RequestBody FullBackup body) throws Exception {
        securityService.validateAdminHeader(client);
        return service.restoreFromFullBackup(body);
    }


    @GetMapping("/backupPosts")
    Aws backupPosts(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.backupPosts();
    }

    @PostMapping("/restorePosts")
    Boolean restorePosts(@RequestHeader String client, @RequestBody Aws body) throws Exception {
        securityService.validateAdminHeader(client);
        return service.restorePosts(body);
    }

    @GetMapping("/getLogs")
    List<Log> getLogs(@RequestHeader String client, @RequestParam(required=false) Integer mode, @RequestParam(required=false) String filter) throws Exception {
        securityService.validateAdminHeader(client);
        return securityService.getLogs(mode, filter);
    }

    @GetMapping("/getAdminMessages")
    List<F1Comment> getAdminMessages(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.getAdminMessages();
    }

    @GetMapping("/endRaceWeekendJobs")
    Boolean endRaceWeekendJobs(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.endRaceWeekendJobs();
    }

    @GetMapping("/exposureDrivers")
    List<ExposureDriver> getExposureDrivers(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.getExposureDrivers();
    }

    @PostMapping("/exposureDrivers")
    List<ExposureDriver> updateExposureDrivers(@RequestHeader String client, @RequestBody List<ExposureDriver> list) throws Exception {
        securityService.validateAdminHeader(client);
        return service.updateExposureDrivers(list);
    }

    @GetMapping("/deleteAwsContent")
    Integer deleteAwsContent(@RequestHeader String client, @RequestParam(required=false) String username) throws Exception {
        securityService.validateAdminHeader(client);
        return service.deleteAwsContent(username);
    }
}
