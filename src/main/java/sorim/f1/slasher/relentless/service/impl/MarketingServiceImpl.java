package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.entities.ImageRow;
import sorim.f1.slasher.relentless.entities.Marketing;
import sorim.f1.slasher.relentless.repository.ImageRepository;
import sorim.f1.slasher.relentless.repository.MarketingRepository;
import sorim.f1.slasher.relentless.service.MarketingService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingServiceImpl implements MarketingService {

    private static final String PREFIX = "MK_AA_";
    private static Integer sequence = 1;
    private final MarketingRepository marketingRepository;
    private final ImageRepository imageRepository;

    @Override
    public Marketing getRandomMarketing() {
        Marketing response;
        response = marketingRepository.findById(sequence++);
        if (response == null) {
            sequence = 1;
            response = marketingRepository.findById(sequence++);
        }
        return response;
    }

    @Override
    public Marketing getMarketing(Integer id) {
        return marketingRepository.findById(id);
    }

    @Override
    public void deleteMarketing(Integer id) {
        marketingRepository.deleteById(id);
    }

    @Override
    public Marketing saveMarketing(Marketing marketing) {
        marketingRepository.save(marketing);
        saveImageToDb(marketing);
        return marketing;
    }

    private void saveImageToDb(Marketing marketing) {
        byte[] image = getImageFromUrl(marketing.getImageUrl());
        ImageRow imageRow = ImageRow.builder().code(PREFIX + marketing.getId()).image(image).build();
        imageRepository.save(imageRow);
    }

    @Override
    public List<Marketing> backupMarketing() {
        return (List<Marketing>) marketingRepository.findAll();
    }

    @Override
    public Boolean restoreMarketing(List<Marketing> marketings) {
        marketingRepository.saveAll(marketings);
        marketings.forEach(this::saveImageToDb);
        return true;
    }


    private byte[] getImageFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try (InputStream stream = url.openStream()) {
                byte[] buffer = new byte[4096];
                while (true) {
                    int bytesRead = stream.read(buffer);
                    if (bytesRead < 0) {
                        break;
                    }
                    output.write(buffer, 0, bytesRead);
                }
            }
            return output.toByteArray();
        } catch (IOException e) {
            log.error("fetchImages ", e);
        }
        return null;
    }
}
