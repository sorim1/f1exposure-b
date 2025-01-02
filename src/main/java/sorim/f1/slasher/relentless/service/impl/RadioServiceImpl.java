package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.service.RadioService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Service
@RequiredArgsConstructor
public class RadioServiceImpl implements RadioService {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public ResponseEntity<byte[]> playMusic() throws IOException {
        for (int i = 0; i < 5; i++) {

        }
        File f = new File("C:\\audiotemp\\" + "3" + ".mp3");
        byte[] file = Files.readAllBytes(f.toPath());

        HttpHeaders headers = new HttpHeaders();
        //headers.set("Content-Disposition", "attachment; filename=\"" + f.getName());
        ResponseEntity<byte[]> response = new ResponseEntity(file, headers, HttpStatus.OK);

        return response;
    }
}
