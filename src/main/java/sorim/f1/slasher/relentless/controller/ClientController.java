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

    @GetMapping("/getStandings")
    AllStandings getStandings(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getStandings();
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

    @PostMapping("/postComment")
    List<F1Comment> postComment(@RequestHeader String client, @RequestBody F1Comment comment, HttpServletRequest request) throws Exception {
        securityService.validateHeader(client);
        String ipAddress = securityService.validateIp(request);
        return service.postComment(comment, ipAddress);
    }

    @PostMapping("/sendMessage")
    Boolean sendMessage(@RequestHeader String client, @RequestBody F1Comment message, HttpServletRequest request) throws Exception {
        securityService.validateHeader(client);
        String ipAddress = securityService.validateIp(request);
        service.sendMessage(message, ipAddress);
        return true;
    }


    @GetMapping("/getComments/{page}")
    List<F1Comment> getComments(@RequestHeader String client, @PathVariable("page") String page) throws Exception {
        securityService.validateHeader(client);
        return service.getComments(page);
    }

    @GetMapping("/fetchInstagramPosts")
    Boolean fetchInstagramFeed(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.fetchInstagramPosts();
    }

    @GetMapping("/fetchTwitterPosts")
    Boolean fetchTwitterPosts(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.fetchTwitterPosts();
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

    @GetMapping(
            value = "/getArt/{code}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    byte[] getArt(@PathVariable("code") String code) throws Exception {
        String cleanedCode = securityService.validateCode(code);
        return service.getArt(cleanedCode);
    }

    @GetMapping("/getTwitterPosts/{page}")
    DoubleTwitterFeed getTwitterPosts(@RequestHeader String client, @PathVariable("page") String page) throws Exception {
        securityService.validateHeader(client);
        return service.getTwitterPosts(Integer.valueOf(page));
    }

    @GetMapping("/getNewRedditPosts/{page}")
    DoubleRedditNewFeed getNewRedditPosts(@RequestHeader String client, @PathVariable("page") Integer page) throws Exception {
        securityService.validateHeader(client);
        return service.getRedditNewPosts(page);
    }

    @GetMapping("/getTopRedditPosts/{page}")
    DoubleRedditTopFeed getRedditTopPosts(@RequestHeader String client, @PathVariable("page") Integer page) throws Exception {
        securityService.validateHeader(client);
        return service.getRedditTopPosts(page);
    }

    @GetMapping("/get4chanPosts/{page}")
    Double4chanFeed get4chanPosts(@RequestHeader String client, @PathVariable("page") String page) throws Exception {
        securityService.validateHeader(client);
        return service.get4chanPosts(Integer.valueOf(page));
    }

    @GetMapping("/fetchRedditPosts")
    Boolean fetchRedditPosts(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.fetchRedditPosts();
        return true;
    }

    @GetMapping("/fetch4chanPosts")
    Boolean fetch4chanPosts(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.fetch4chanPosts();
    }

    @PostMapping("/postAwsContent")
    String postContent(@RequestHeader String client, @RequestBody AwsContent content, HttpServletRequest request) throws Exception {
        securityService.validateHeader(client);
        String ipAddress = securityService.validateIp(request);
        return service.postContent(content, ipAddress);
    }

    @GetMapping("/getNews/{page}")
    List<AwsContent> getNews(@RequestHeader String client, @PathVariable("page") Integer page) throws Exception {
        securityService.validateHeader(client);
        return service.getNews(page);
    }

    @GetMapping("/getUtilityContext")
    UtilityContext getUtilityContext(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getUtilityContext();
    }

    @GetMapping("/getTopNews")
    AwsContent getTopNews(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getTopNews();
    }

    @GetMapping("/getAwsPost/{code}")
    AwsContent getAwsPost(@RequestHeader String client, @PathVariable("code") String code) throws Exception {
        securityService.validateHeader(client);
        return service.getAwsPost(code);
    }

    @PostMapping("/postAwsComment")
    List<AwsComment> postAwsComment(@RequestHeader String client, @RequestBody AwsComment comment, HttpServletRequest request) throws Exception {
        securityService.validateHeader(client);
        String ipAddress = securityService.validateIp(request);
        return service.postAwsComment(comment, ipAddress);
    }

    @GetMapping("/getAwsComments/{code}")
    List<AwsComment> getAwsComments(@RequestHeader String client, @PathVariable("code") String code) throws Exception {
        securityService.validateHeader(client);
        return service.getAwsComments(code);
    }

    @PostMapping("/moderateComment")
    BasicResponse moderateComment(@RequestHeader String client, @RequestBody CommentModeration moderation) throws Exception {
        securityService.validateHeader(client);
        return service.moderateComment(moderation);
    }

    @GetMapping("/getReplays/{page}")
    List<Replay> getReplays(@RequestHeader String client, @PathVariable("page") Integer page) throws Exception {
        securityService.validateHeader(client);
        return service.getReplays(page);
    }

    @GetMapping("/getStreamer")
    BasicResponse getStreamer(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return BasicResponse.builder().message(service.getStreamer()).build();
    }

    @GetMapping("/updateStreamer/{streamer}")
    Boolean getStreamer(@RequestHeader String client, @PathVariable("streamer") String streamer) throws Exception {
        securityService.validateHeader(client);
        return service.setStreamer(streamer);
    }
}
