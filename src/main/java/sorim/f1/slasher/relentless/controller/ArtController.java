package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sorim.f1.slasher.relentless.entities.ArtImageRow;
import sorim.f1.slasher.relentless.service.ArtService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("art")
public class ArtController {

    private final SecurityService securityService;
    private final ArtService artService;


    @GetMapping(
            value = "/generateImage",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    byte[] generateImage(@RequestParam Integer xDrag, @RequestParam Integer yDrag, @RequestParam Integer maxIteration, @RequestParam Integer diameter) throws IOException {
        return artService.generateImage(xDrag, yDrag, maxIteration, diameter);
    }

    @GetMapping("/executeArt")
    Boolean executeArt() throws IOException {
        return artService.executeArt();
    }

    @GetMapping("/generateLatestArt")
    Boolean generateLatestArt(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return artService.generateLatestArt();
    }

    @GetMapping("/generateLatestArtForced")
    Boolean generateLatestArtForced(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return artService.generateLatestArtForced();
    }

    @PostMapping("/updateArt/{code}")
    Boolean updateLatestArt(@RequestHeader String client, @RequestParam("image") MultipartFile image,  @PathVariable("code") String code) throws Exception {
        securityService.validateAdminHeader(client);
        byte[] bytes = image.getBytes();
        return artService.updateArt(code, bytes);
    }

    @PostMapping("/saveImage/{code}")
    Boolean saveImage(@RequestHeader String client, @RequestParam("image") MultipartFile image,  @PathVariable("code") String code) throws Exception {
        securityService.validateAdminHeader(client);
        byte[] bytes = image.getBytes();
        return artService.saveImage(code, bytes);
    }

    @GetMapping("/setLatestArt/{code}")
    Boolean setLatestArt(@RequestHeader String client, @PathVariable("code") String code) throws Exception {
        securityService.validateAdminHeader(client);
        return artService.setLatestArt(code);
    }
    @GetMapping("/deleteArt/{code}")
    Boolean deleteArt(@RequestHeader String client, @PathVariable("code") String code) throws Exception {
        securityService.validateAdminHeader(client);
        return artService.deleteArt(code);
    }
    @GetMapping("/getAllArt")
    List<ArtImageRow> getAllArt(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return artService.getAllArt();
    }

    @PostMapping("/postArt")
    ArtImageRow postArt(@RequestHeader String client, @RequestBody ArtImageRow body) throws Exception {
        securityService.validateAdminHeader(client);
        return artService.postArt(body);
    }
}
