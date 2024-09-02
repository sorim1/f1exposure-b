package sorim.f1.slasher.relentless.controller;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.Aws;
import sorim.f1.slasher.relentless.model.FullBackup;
import sorim.f1.slasher.relentless.model.FullExposure;
import sorim.f1.slasher.relentless.model.SearchParams;
import sorim.f1.slasher.relentless.service.AdminService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("sorimzone")
public class AdminController {

    private final AdminService service;
    private final SecurityService securityService;

    @PostMapping("/refreshCalendar")
    boolean refreshCalendar(@RequestHeader String client, @RequestBody(required = false) String url) throws Exception {
        securityService.validateAdminHeader(client);
        return service.refreshCalendarOfCurrentSeason(url);
    }

    @GetMapping("/getCalendar")
    F1Calendar getCalendar(@RequestHeader String client) throws Exception {
        return service.getCalendar();
    }

    @PostMapping("/saveCalendar")
    F1Calendar saveCalendar(@RequestHeader String client, @RequestBody F1Calendar body) throws Exception {
        securityService.validateAdminHeader(client);
        return service.saveCalendar(body);
    }

    @PostMapping("/refreshCalendarSecondary")
    boolean refreshCalendarSecondary(@RequestHeader String client, @RequestBody(required = false) String url) throws Exception {
        securityService.validateAdminHeader(client);
        return service.refreshCalendarOfCurrentSeasonSecondary(url);
    }

    @GetMapping("/deleteCalendar")
    boolean deleteCalendar(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.deleteCalendar();
    }

    @PostMapping("/setOverlays")
    String setCountdownModes(@RequestHeader String client, @RequestBody String overlays) throws Exception {
        securityService.validateAdminHeader(client);
        return service.setOverlays(overlays);
    }

    @PostMapping("/setIframeLink")
    String setIframeLink(@RequestHeader String client, @RequestBody String body) throws Exception {
        securityService.validateAdminHeader(client);
        return service.setIframeLink(body);
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

    @GetMapping("/initialize")
    boolean initialize(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.initialize();
        return true;
    }

    @GetMapping("/initializeStandings")
    Boolean intializeStandings(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.initializeStandings(true);
    }

    @GetMapping("/initializeFullStandingsThroughRounds")
    Boolean initializeFullStandingsThroughRounds(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.initializeFullStandingsThroughRounds();
    }

    @GetMapping("/fetchFourChan")
    Boolean fetchFourChan(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.fetchFourChanPosts();
    }

    @GetMapping("/deleteFourChan")
    Boolean deleteFourChan(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.deleteFourChanPosts();
    }

    @GetMapping("/deleteFourChanPost/{id}")
    Boolean deleteFourChanPost(@RequestHeader String client, @PathVariable("id") Integer id) throws Exception {
        securityService.validateAdminHeader(client);
        return service.deleteFourChanPost(id);
    }

    @PostMapping("/reverseGoogleImage")
    Boolean reverseGoogleImage(@RequestHeader String client, @RequestBody String url) throws Exception {
        securityService.validateAdminHeader(client);
        return service.reverseGoogleImage(url);
    }

    @GetMapping("/fetchReplayLinks")
    void fetchReplayLinks(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.fetchReplayLinks();
    }

    @GetMapping("/removeVideo/{id}")
    Boolean removeVideo(@RequestHeader String client, @PathVariable("mode") Integer id) throws Exception {
        securityService.validateAdminHeader(client);
        return service.removeVideo(id);
    }

    @PostMapping("/saveVideos")
    List<Replay> saveVideos(@RequestHeader String client, @RequestBody List<Replay> videos) throws Exception {
        securityService.validateAdminHeader(client);
        return service.saveVideos(videos);
    }

    @GetMapping("/deleteComment/{mode}/{id}")
    Integer deleteComment(@RequestHeader String client, @PathVariable("mode") String mode, @PathVariable("id") String id) throws Exception {
        securityService.validateAdminHeader(client);
        return service.deleteComment(Integer.valueOf(mode), Integer.valueOf(id));
    }

    @PatchMapping("/updateAwsPost/")
    NewsContent updateNewsPost(@RequestHeader String client, @RequestBody NewsContent entry) throws Exception {
        securityService.validateHeader(client);
        return service.patchAwsPost(entry);
    }

    @PostMapping("/findNewsPosts")
    List<NewsContent> findNewsPosts(@RequestHeader String client, @RequestBody SearchParams searchParams) throws Exception {
        securityService.validateHeader(client);
        return service.findNewsPosts(searchParams);
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
    List<Log> getLogs(@RequestHeader String client, @RequestParam(required = false) Integer mode, @RequestParam(required = false) String filter) throws Exception {
        securityService.validateAdminHeader(client);
        return securityService.getLogs(mode, filter);
    }

    @GetMapping("/getAdminMessages")
    List<F1Comment> getAdminMessages(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.getAdminMessages();
    }

    @GetMapping("/generateChart")
    Boolean generateChart(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.generateChart();
    }

    @GetMapping("/endRaceWeekendJobs")
    Boolean endRaceWeekendJobs(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.endRaceWeekendJobs();
    }

    @GetMapping("/exposureDrivers")
    List<Driver> getExposureDrivers(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.getExposureDrivers();
    }

    @PostMapping("/exposureDrivers")
    List<Driver> updateExposureDrivers(@RequestHeader String client, @RequestBody List<Driver> list) throws Exception {
        securityService.validateAdminHeader(client);
        return service.updateExposureDrivers(list);
    }

    @GetMapping("/deleteAwsContent")
    Integer deleteAwsContent(@RequestHeader String client, @RequestParam(required = false) String username) throws Exception {
        securityService.validateAdminHeader(client);
        return service.deleteAwsContent(username);
    }

    @GetMapping("/cleanup")
    Boolean cleanup(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.cleanup();
    }

    @GetMapping("/instagramCleanup")
    Boolean instagramCleanup(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.instagramCleanup();
    }

    @GetMapping("/twitterCleanup")
    Boolean twitterCleanup(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.twitterCleanup();
    }

    @GetMapping("/setInstagramWorkerPassword/{password}")
    String setInstagramWorkerPassword(@RequestHeader String client, @PathVariable String password) throws Exception {
        securityService.validateAdminHeader(client);
        return service.setInstagramWorkerPassword(password);
    }

    @GetMapping("/setInstagramWorker/{username}/{password}")
    String setInstagramWorker2Password(@RequestHeader String client, @PathVariable String username, @PathVariable String password) throws Exception {
        securityService.validateAdminHeader(client);
        return service.setInstagramWorker(username, password);
    }

    @GetMapping("/instagram/{bool}")
    String turnOnOffInstagram(@RequestHeader String client, @PathVariable Boolean bool) throws Exception {
        securityService.validateAdminHeader(client);
        return service.turnOnOffInstagram(bool);
    }

    @GetMapping("/checkCurrentStream")
    Boolean checkCurrentStream() throws IOException {
        return service.checkCurrentStream();
    }

    @GetMapping("/updateCurrentSeasonPast/{season}")
    String updateCurrentSeasonPast(@RequestHeader String client, @PathVariable("season") Integer season) {
        return service.updateCurrentSeasonPast(season);
    }

    @GetMapping("/saveProperty/{name}/{value}")
    String saveProperty(@RequestHeader String client, @PathVariable String name, @PathVariable String value) throws Exception {
        securityService.validateAdminHeader(client);
        return service.saveProperty(name, value);
    }


    @PostMapping("/updateJsonRepository")
    JsonRepositoryModel updateJsonRepository(@RequestHeader String client, @RequestBody JsonRepositoryModel body) throws Exception {
        securityService.validateAdminHeader(client);
        return service.updateJsonRepository(body);
    }

    @GetMapping("/postFormulaDankToInstagram")
    String postFormulaDankToInstagram(@RequestHeader String client) throws IGLoginException {
        return service.postFormulaDankToInstagram();
    }

    @GetMapping("/getJsonRepository/{id}")
    JsonRepositoryModel getJsonRepository(@PathVariable("id") String id) {
        return service.getJsonRepository(id);
    }

    @GetMapping("/deleteJsonRepository/{id}")
    Boolean deleteJsonRepository(@RequestHeader String client, @PathVariable("id") String id) {
        return service.deleteJsonRepository(id);
    }

    @PostMapping("/updateJsonRepositoryTwo")
    JsonRepositoryTwoModel updateJsonRepositoryTwo(@RequestBody JsonRepositoryTwoModel body) {
        return service.updateJsonRepositoryTwo(body);
    }

    @GetMapping("/getJsonRepositoryTwo/{id}")
    Object getJsonRepositoryTwo(@PathVariable("id") String id) {
        return service.getJsonRepositoryTwo(id).getJson();
    }

}
