package sorim.f1.slasher.relentless.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void save(MultipartFile file);

    Resource load(String filename);

    void deleteAll();
}
