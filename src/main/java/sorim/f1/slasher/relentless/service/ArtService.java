package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ArtImageRow;

import java.util.List;

public interface ArtService {

    Boolean updateImage(String code, byte[] image);

    Boolean saveImage(String code, byte[] image);

    List<ArtImageRow> getAllImages();

    ArtImageRow postImage(ArtImageRow body);

    void restoreAllImages(List<ArtImageRow> artBackup);

    Boolean deleteImage(String code);

    Boolean deleteImagesExceptM();
}
