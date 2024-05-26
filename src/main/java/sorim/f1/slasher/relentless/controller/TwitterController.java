package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sorim.f1.slasher.relentless.entities.TwitterPost;
import sorim.f1.slasher.relentless.service.SecurityService;
import sorim.f1.slasher.relentless.service.TwitterService;

import java.util.List;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("twitter")
public class TwitterController {

    private final SecurityService securityService;
    private final TwitterService service;

    @GetMapping("/getTwitterFerrariPosts")
    public List<TwitterPost> getTwitterFerrariPosts(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getTwitterFerrariPosts();
    }
}
