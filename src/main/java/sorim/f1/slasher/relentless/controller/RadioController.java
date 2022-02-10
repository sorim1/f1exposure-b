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
import sorim.f1.slasher.relentless.model.FileData;
import sorim.f1.slasher.relentless.model.UploadResponseMessage;
import sorim.f1.slasher.relentless.service.FileService;
import sorim.f1.slasher.relentless.service.RadioService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("radio")
public class RadioController {

    private final SecurityService securityService;
    private final RadioService service;

    @GetMapping("/playMusic")
    public ResponseEntity<byte[]> playMusic() throws IOException {
        log.info("playMusic");
        return service.playMusic();
    }
}
