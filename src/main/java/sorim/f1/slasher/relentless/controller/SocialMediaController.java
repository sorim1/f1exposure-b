package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import sorim.f1.slasher.relentless.model.KeyValue;
import sorim.f1.slasher.relentless.service.FourchanService;
import sorim.f1.slasher.relentless.service.InstagramService;
import sorim.f1.slasher.relentless.service.SecurityService;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("social")
public class SocialMediaController {

    private final SecurityService securityService;
    private final FourchanService fourchanService;
    private final InstagramService instagramService;

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImage(@PathVariable("id") Integer id) {
        return fourchanService.getChanImage(id);
    }

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
    String setNoDuplicatesFound(@RequestHeader String client, @RequestBody String newValue) throws Exception {
        securityService.validateAdminHeader(client);
        return fourchanService.setNoDuplicatesFound(newValue);
    }

    @PostMapping("/saveChanPosts")
    List<FourChanPostEntity> saveChanPosts(@RequestHeader String client, @RequestHeader String username, @RequestBody List<FourChanPostEntity> body) throws Exception {
        securityService.validateClientAndUsername(client, username);
        return fourchanService.saveChanPosts(body);
    }

    @GetMapping("/deleteChanByStatus/{status}")
    Boolean deleteChanByStatus(@RequestHeader String client, @PathVariable Integer status) throws Exception {
        securityService.validateAdminHeader(client);
        return fourchanService.deleteChanByStatus(status);
    }

    @GetMapping("/getInstagramFollows")
    List<KeyValue> getInstagramFollows(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return instagramService.getInstagramFollows();
    }

    @GetMapping("/postToInstagram")
    String postToInstagram(@RequestHeader String client, @RequestParam Boolean personalMeme) throws Exception {
        securityService.validateAdminHeader(client);
        return fourchanService.postToInstagram(personalMeme);
    }

    @PostMapping("/saveChanImages")
    List<String> saveChanImages(@RequestHeader String client, @RequestParam("image") MultipartFile[] images) throws Exception {
        securityService.validateAdminHeader(client);
        return fourchanService.saveChanImages(images);
    }

    @GetMapping("/followMoreOnInstagram")
    Boolean followMoreOnInstagram(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        instagramService.followMoreOnInstagram();
        return true;
    }

    @GetMapping("/cleanup")
    Boolean cleanup(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        fourchanService.cleanup();
        return true;
    }

    @GetMapping("/deleteByStatus/{status}")
    Boolean cleanup(@RequestHeader String client, @PathVariable Integer status) throws Exception {
        securityService.validateAdminHeader(client);
        fourchanService.deleteByStatus(status);
        return true;
    }

    @GetMapping("/downloadPngs")
    public void downloadPngs(@RequestHeader String client, HttpServletResponse response) throws Exception {
        securityService.validateAdminHeader(client);
        fourchanService.getAcceptedPngImages(response);
    }
}
