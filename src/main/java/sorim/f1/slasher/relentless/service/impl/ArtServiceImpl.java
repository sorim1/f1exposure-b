package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sorim.f1.slasher.relentless.entities.ArtImageRow;
import sorim.f1.slasher.relentless.entities.ImageRow;
import sorim.f1.slasher.relentless.repository.ArtImageRepository;
import sorim.f1.slasher.relentless.repository.ImageRepository;
import sorim.f1.slasher.relentless.service.ArtService;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArtServiceImpl implements ArtService {
    private final ArtImageRepository artImageRepository;
    private final ImageRepository imageRepository;

    @Override
    public Boolean deleteImage(String code) {
        artImageRepository.deleteByCode(code);
        return true;
    }

    @Override
    public Boolean deleteImagesExceptM() {
        artImageRepository.deleteAll();
        imageRepository.deleteEverythingExceptM();
        return true;
    }

    @Override
    public Boolean updateImage(String code, byte[] image) {
        ArtImageRow row = artImageRepository.findFirstByCode(code);
        row.setImage(image);
        artImageRepository.save(row);
        return true;
    }

    @Override
    public Boolean saveImage(String code, MultipartFile image) throws IOException {
        ImageRow imageRow = ImageRow.builder().code(code).image(image.getBytes())
                .type(image.getContentType())
                .build();
        imageRepository.save(imageRow);
        return true;
    }

    @Override
    public List<ArtImageRow> getAllImages() {
        return artImageRepository.findAllByOrderBySeasonDescRoundDesc();
    }

    @Override
    public ArtImageRow postImage(ArtImageRow body) {
        ArtImageRow row = artImageRepository.findFirstByCode(body.getCode());
        if (row == null) {
            artImageRepository.save(body);
            return body;
        } else {
            row.update(body);
            artImageRepository.save(row);
            return row;
        }
    }

    @Override
    public void restoreAllImages(List<ArtImageRow> artBackup) {
        artImageRepository.saveAll(artBackup);
    }
}
