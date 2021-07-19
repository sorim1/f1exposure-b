package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.service.ClientService;
import sorim.f1.slasher.relentless.service.SecurityService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("f1exposure")
public class ClientController {

    private final ClientService service;
    private final SecurityService securityService;

    @GetMapping("/countdown")
    CalendarData getCountdownData(@RequestHeader String client, @RequestParam String mode) throws Exception {
        securityService.validateHeader(client);
        return service.getCountdownData(Integer.valueOf(mode));
    }

    @GetMapping("/getExposureDriverList")
    ExposureResponse getExposureDriverList(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getExposureDriverList();
    }

    @PostMapping("/expose")
    Boolean exposeDrivers(@RequestHeader String client, @RequestBody String[] exposedList, HttpServletRequest request) throws Exception {
        securityService.validateHeader(client);
        String ipAddress = securityService.validateIp(request);
        return service.exposeDrivers(exposedList, ipAddress);
    }

    @GetMapping("/getExposureResults")
    ExposureData getExposedChartData(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getExposedChartData();
    }


    @GetMapping("/getDriverStandings")
    List<DriverStanding> getDriverStandings(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getDriverStandings();
    }

    @GetMapping("/getConstructorStandings")
    List<ConstructorStanding> getConstructorStandings(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getConstructorStandings();
    }

    @GetMapping("/getStandings")
    AllStandings getStandings(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getStandings();
    }

    @GetMapping("/getSportSurge")
    List<SportSurgeEvent> getSportSurge(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getSportSurge();
    }

    @PostMapping("/postComment")
    List<F1Comment> postComment(@RequestHeader String client, @RequestBody F1Comment comment) throws Exception {
        securityService.validateHeader(client);
        return service.postComment(comment);
    }

    @GetMapping("/getComments/{page}")
    List<F1Comment> getComments(@RequestHeader String client, @PathVariable("page") String page) throws Exception {
        securityService.validateHeader(client);
        return service.getComments(page);
    }

    @GetMapping("/fetchInstagramFeed")
    List<InstagramPost> fetchInstagramFeed(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.fetchInstagramFeed();
    }

    @GetMapping("/getInstagramFeed")
    TripleInstagramFeed getInstagramFeed(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getInstagramFeed();
    }

    @GetMapping("/getInstagramPosts/{page}")
    TripleInstagramFeed getInstagramFeedPage(@RequestHeader String client, @PathVariable("page") String page) throws Exception {
        securityService.validateHeader(client);
        return service.getInstagramFeedPage(Integer.valueOf(page));
    }

    @GetMapping(
            value = "/getImage/{code}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    byte[] getImage(@PathVariable("code") String code) {
        return service.getImage(code);
    }

    @GetMapping("/getTwitterPosts/{page}")
    DoubleTwitterFeed getTwitterPosts(@RequestHeader String client, @PathVariable("page") String page) throws Exception {
        securityService.validateHeader(client);
        return service.getTwitterPosts(Integer.valueOf(page));
    }

    @GetMapping("/fetchTwitterPosts")
    List<TwitterPost> fetchTwitterPosts(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.fetchTwitterPosts();
    }
}
