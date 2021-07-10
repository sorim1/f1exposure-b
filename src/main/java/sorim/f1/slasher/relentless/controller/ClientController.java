package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.service.ClientService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ClientController {

    private final ClientService service;

    @GetMapping("/getExposureDriverList")
    ExposureResponse getExposureDriverList(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.getExposureDriverList();
    }

    @PostMapping("/expose")
    Boolean exposeDrivers(@RequestHeader String authorization, @RequestBody String[] exposedList, HttpServletRequest request) throws Exception {
        service.validateHeader(authorization);
        String ipAddress = service.validateIp(request);
        return service.exposeDrivers(exposedList, ipAddress);
    }

    @GetMapping("/exposed")
    ExposedChart getExposedChartData(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.getExposedChartData();
    }

    @GetMapping("/countdown")
    CalendarData getCountdownData(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.getCountdownData();
    }

    @GetMapping("/getDriverStandings")
    List<DriverStanding> getDriverStandings(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.getDriverStandings();
    }

    @GetMapping("/getConstructorStandings")
    List<ConstructorStanding> getConstructorStandings(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.getConstructorStandings();
    }

    @GetMapping("/getStandings")
    AllStandings getStandings(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.getStandings();
    }

    @GetMapping("/getSportSurge")
    List<SportSurgeEvent> getSportSurge(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.getSportSurge();
    }

    @PostMapping("/postComment")
    List<F1Comment> postComment(@RequestHeader String authorization, @RequestBody F1Comment comment) throws Exception {
        service.validateHeader(authorization);
        return service.postComment(comment);
    }

    @GetMapping("/getComments/{page}")
    List<F1Comment> getComments(@RequestHeader String authorization, @PathVariable("page") String page) throws Exception {
        service.validateHeader(authorization);
        return service.getComments(page);
    }

    @GetMapping("/fetchInstagramFeed")
    List<InstagramPost> fetchInstagramFeed(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.fetchInstagramFeed();
    }

    @GetMapping("/getInstagramFeed")
    TripleInstagramFeed getInstagramFeed(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.getInstagramFeed();
    }

    @GetMapping("/getInstagramPosts/{page}")
    TripleInstagramFeed getInstagramFeedPage(@RequestHeader String authorization, @PathVariable("page") String page) throws Exception {
        log.info("getInstagramFeedPage: {}", page);
        service.validateHeader(authorization);
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
    DoubleTwitterFeed getTwitterPosts(@RequestHeader String authorization, @PathVariable("page") String page) throws Exception {
        service.validateHeader(authorization);
        return service.getTwitterPosts(Integer.valueOf(page));
    }

    @GetMapping("/fetchTwitterPosts")
    List<TwitterPost> fetchTwitterPosts(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.fetchTwitterPosts();
    }
}
