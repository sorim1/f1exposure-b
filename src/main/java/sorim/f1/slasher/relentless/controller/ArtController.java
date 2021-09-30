package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import sorim.f1.slasher.relentless.entities.F1Calendar;
import sorim.f1.slasher.relentless.model.FileData;
import sorim.f1.slasher.relentless.model.UploadResponseMessage;
import sorim.f1.slasher.relentless.service.ArtService;
import sorim.f1.slasher.relentless.service.FileService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


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
    byte[] generateImage() throws IOException {
        return artService.generateImage();
    }
}
