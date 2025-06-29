package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.service.ClientService;
import sorim.f1.slasher.relentless.service.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
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

    @GetMapping("/getFourchanDisabled")
    Boolean getFourchanDisabled() {
        return service.getFourchanDisabled();
    }

    @GetMapping("/setFourchanDisabled/{value}")
    Boolean setFourchanDisabled(@PathVariable("value") String value) {
        return service.setFourchanDisabled(value);
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
        securityService.validateHeader(client);
        return service.fetchTwitterPosts();
    }

    @GetMapping("/getInstagramPosts/{mode}/{page}")
    TripleInstagramFeed getInstagramFeedPage(@RequestHeader String client, @PathVariable("mode") Integer mode, @PathVariable("page") Integer page) throws Exception {
        securityService.validateHeader(client);
        return service.getInstagramFeedPage(mode, page);
    }

    @GetMapping("/image/{code}")
    public ResponseEntity<byte[]> getImage(@PathVariable("code") String code) {
        ImageRow image = service.getImage(code);
        byte[] imageBytes = image.getImage();
        HttpHeaders headers = new HttpHeaders();
        if(image.getType()!=null){
            headers.setContentType(MediaType.parseMediaType(image.getType()));
        } else {
            headers.setContentType(MediaType.IMAGE_JPEG);
        }
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @GetMapping(
            value = "/getArt/{code}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    byte[] getArt(@PathVariable("code") String code) throws Exception {
        String cleanedCode = securityService.validateCode(code);
        return service.getArt(cleanedCode);
    }

    @GetMapping("/getTwitterPosts/{mode}/{page}")
    TrippleTwitterFeed getTwitterPosts(@RequestHeader String client, @PathVariable("mode") Integer mode, @PathVariable("page") Integer page) throws Exception {
        securityService.validateHeader(client);
        return service.getTwitterPosts(mode, page);
    }

    @PostMapping("/setTwitterEndpoints")
    List<String> setTwitterEndpoints(@RequestHeader String client, @RequestBody List<String> newEndpoints) throws Exception {
        securityService.validateAdminHeader(client);
        return service.setTwitterEndpoints(newEndpoints);
    }

    @GetMapping("/getRedditPosts/{mode}/{page}")
    TrippleRedditFeed getNewRedditPosts(@RequestHeader String client, @PathVariable("mode") Integer mode, @PathVariable("page") Integer page) throws Exception {
        securityService.validateHeader(client);
        return service.getRedditPosts(mode, page);
    }

    @GetMapping("/get4chanPosts/{mode}/{page}")
    Tripple4chanFeed get4chanPosts(@RequestHeader String client, @PathVariable("mode") Integer mode, @PathVariable("page") Integer page) throws Exception {
        securityService.validateHeader(client);
        return service.get4chanPosts(mode, page);
    }

    @GetMapping("/getStreamables")
    List<Streamable> getStreamables(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getStreamables();
    }

    @GetMapping("/fetchRedditPosts")
    Boolean fetchRedditPosts(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        service.fetchRedditPosts();
        return true;
    }


    @GetMapping("/fetch4chanPosts")
    Boolean fetch4chanPosts(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.fetch4chanPosts();
    }

    @PostMapping("/postNewsContent")
    String postContent(@RequestHeader String client, @RequestBody NewsContent content, HttpServletRequest request) throws Exception {
        securityService.validateHeader(client);
        String ipAddress = securityService.validateIp(request);
        return service.postContent(content, ipAddress);
    }

    @GetMapping("/getNews/{page}")
    List<NewsContent> getNews(@RequestHeader String client, @PathVariable("page") Integer page) throws Exception {
        securityService.validateHeader(client);
        return service.getNews(page);
    }

    @GetMapping("/getUtilityContext")
    UtilityContext getUtilityContext(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getUtilityContext();
    }

    @GetMapping("/getSidebarData")
    SidebarData getSidebarData(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getSidebarData();
    }

    @PostMapping("/setSidebarExposureDriver")
    SidebarData setSidebarExposureDriver(@RequestHeader String client, @RequestBody(required = false) KeyValue exposureDriver) throws Exception {
        securityService.validateHeader(client);
        return service.setSidebarExposureDriver(exposureDriver);
    }

    @PostMapping("/setSidebarStrawpoll")
    SidebarData setSidebarStrawpoll(@RequestHeader String client, @RequestBody(required = false) KeyValue strawpoll) throws Exception {
        securityService.validateHeader(client);
        return service.setSidebarStrawpoll(strawpoll);
    }

    @GetMapping("/getStrawpoll")
    KeyValue getStrawpoll(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getStrawpoll();
    }

    @GetMapping("/getNewsPost/{code}")
    NewsContent getNewsPost(@RequestHeader String client, @PathVariable("code") String code) throws Exception {
        securityService.validateHeader(client);
        return service.getNewsPost(code);
    }

    @GetMapping("/getNextThreeNews/{timestampActivity}")
    List<NewsContent> getNextNewsList(@RequestHeader String client, @PathVariable("timestampActivity") String timestampActivity) throws Exception {
        securityService.validateHeader(client);
        return service.getNextNewsList(timestampActivity);
    }

    @GetMapping("/getCountdownFooterData")
    List<NavbarData> getNavbarData(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getCountdownFooterData();
    }

    @GetMapping("/updateCountdownFooterData")
    List<NavbarData> updateCountdownFooterData(@RequestHeader String client, @RequestBody List<NavbarData> countdownFooterData) throws Exception {
        securityService.validateHeader(client);
        return service.updateCountdownFooterData(countdownFooterData);
    }

    @GetMapping("/bumpNewsPost/{code}/{mode}")
    Boolean bumpNewsPost(@RequestHeader String client, @PathVariable("code") String code, @PathVariable("mode") Integer mode) throws Exception {
        securityService.validateHeader(client);
        return service.bumpNewsPost(code, mode);
    }


    @PostMapping("/postNewsComment")
    NewsComment postNewsComment(@RequestHeader String client, @RequestBody NewsComment comment, HttpServletRequest request) throws Exception {
        securityService.validateHeader(client);
        String ipAddress = securityService.validateIp(request);
        return service.postNewsComment(comment, ipAddress);
    }

    @GetMapping("/getNewsComments/{code}")
    List<NewsComment> getNewsComments(@RequestHeader String client, @PathVariable("code") String code) throws Exception {
        securityService.validateHeader(client);
        return service.getNewsComments(code);
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

    @GetMapping("/getVideos")
    List<Replay> getVideos(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getVideos();
    }

    @GetMapping("/setAllowNonRedditNewsProperty/{bool}")
    Boolean setAllowNonRedditNewsProperty(@RequestHeader String client, @PathVariable("bool") Boolean bool) throws Exception {
        securityService.validateAdminHeader(client);
        service.setAllowNonRedditNewsProperty(bool);
        return bool;
    }
}
