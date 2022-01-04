package sorim.f1.slasher.relentless.service;

import java.io.IOException;

public interface TwitchService {

    Boolean setStreamer(String username);

    Boolean checkCurrentStream() throws IOException;

    String getStreamer();

}
