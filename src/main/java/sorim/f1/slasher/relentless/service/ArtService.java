package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ArtImageRow;

import java.io.IOException;
import java.util.List;

public interface ArtService {

    Boolean executeArt() throws IOException;

    Boolean generateLatestArt() throws IOException;

    Boolean generateLatestArtForced() throws IOException;

    byte[] generateImage(Integer xDrag, Integer yDrag, Integer maxIteration, Integer diameter) throws IOException;

    Boolean updateArt(String code, byte[] image);

    Boolean saveImage(String code, byte[] image);

    List<ArtImageRow> getAllArt();

    ArtImageRow postArt(ArtImageRow body);

    void restoreAllArt(List<ArtImageRow> artBackup);

    Boolean setLatestArt(String code);
}
