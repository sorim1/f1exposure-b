package sorim.f1.slasher.relentless.service;

import java.io.IOException;

public interface ArtService {

    Boolean executeArt() throws IOException;

    byte[] generateImage(Integer xDrag, Integer yDrag, Integer maxIteration, Integer diameter) throws IOException;
}
