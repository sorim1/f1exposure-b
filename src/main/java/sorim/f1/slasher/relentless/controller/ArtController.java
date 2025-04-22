package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sorim.f1.slasher.relentless.entities.ArtImageRow;
import sorim.f1.slasher.relentless.service.ArtService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.util.List;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("art")
public class ArtController {

    private final SecurityService securityService;
    private final ArtService artService;

    @PostMapping("/updateImage/{code}")
    Boolean updateImage(@RequestHeader String client, @RequestParam("image") MultipartFile image, @PathVariable("code") String code) throws Exception {
        securityService.validateAdminHeader(client);
        byte[] bytes = image.getBytes();
        return artService.updateImage(code, bytes);
    }

    @PostMapping("/saveImage/{code}")
    Boolean saveImage(@RequestHeader String client, @RequestParam("image") MultipartFile image, @PathVariable("code") String code) throws Exception {
        securityService.validateAdminHeader(client);
        return artService.saveImage(code, image);
    }

    @PostMapping("/saveCommentImage/{code}")
    Boolean saveCommentImage(@RequestHeader String client, @RequestParam("image") MultipartFile image, @PathVariable("code") String code) throws Exception {
        securityService.validateHeader(client);
        String newsCode = "comment_" + code;
        return artService.saveImage(newsCode, image);
    }

    @GetMapping("/deleteImage/{code}")
    Boolean deleteImage(@RequestHeader String client, @PathVariable("code") String code) throws Exception {
        securityService.validateAdminHeader(client);
        return artService.deleteImage(code);
    }

    @GetMapping("/deleteImagesExceptM")
    Boolean deleteImagesExceptM(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return artService.deleteImagesExceptM();
    }

    @GetMapping("/getAllImages")
    List<ArtImageRow> getAllImages(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return artService.getAllImages();
    }

    @PostMapping("/postImage")
    ArtImageRow postImage(@RequestHeader String client, @RequestBody ArtImageRow body) throws Exception {
        securityService.validateAdminHeader(client);
        return artService.postImage(body);
    }
}
