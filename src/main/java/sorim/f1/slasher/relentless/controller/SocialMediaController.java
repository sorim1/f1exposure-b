package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import sorim.f1.slasher.relentless.model.FileData;
import sorim.f1.slasher.relentless.model.FourchanPost;
import sorim.f1.slasher.relentless.model.UploadResponseMessage;
import sorim.f1.slasher.relentless.service.FileService;
import sorim.f1.slasher.relentless.service.FourchanService;
import sorim.f1.slasher.relentless.service.InstagramService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("social")
public class SocialMediaController {

    private final SecurityService securityService;
    private final FourchanService fourchanService;
    private final InstagramService instagramService;

    @GetMapping("/getChanPostsByStatus/{status}")
    List<FourChanPostEntity> getChanPostsByStatus(@RequestHeader String client, @RequestHeader String username, @PathVariable("status") Integer status) throws Exception {
        securityService.validateClientAndUsername(client, username);
        return fourchanService.getChanPostsByStatus(status);
    }

    @GetMapping("/getChanPostsSums")
    List<Integer> getChanPostsSums(@RequestHeader String client, @RequestHeader String username) throws Exception {
        securityService.validateClientAndUsername(client, username);
        return fourchanService.getChanPostsSums();
    }

    @PostMapping("/setNoDuplicatesFound")
    String setNoDuplicatesFound(@RequestHeader String client,@RequestBody String newValue) throws Exception {
        securityService.validateAdminHeader(client);
        return fourchanService.setNoDuplicatesFound(newValue);
    }

    @PostMapping("/saveChanPosts")
    List<FourChanPostEntity> saveChanPosts(@RequestHeader String client, @RequestHeader String username, @RequestBody List<FourChanPostEntity> body) throws Exception {
        securityService.validateClientAndUsername(client, username);
        return fourchanService.saveChanPosts(body);
    }
    @GetMapping("/getInstagramFollows")
    List<String> getInstagramFollows(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return instagramService.getInstagramFollows();
    }
}
