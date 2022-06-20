package sorim.f1.slasher.relentless.service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface RadioService {

    ResponseEntity<byte[]> playMusic() throws IOException;

}
