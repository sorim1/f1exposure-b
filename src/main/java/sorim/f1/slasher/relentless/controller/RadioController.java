package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sorim.f1.slasher.relentless.service.RadioService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
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
