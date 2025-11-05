package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sorim.f1.slasher.relentless.service.FileService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileServiceImpl implements FileService {

    @Value("${upload.path}")
    private String uploadPath;

    private Path rootPath;

    @PostConstruct
    public void init() {
        rootPath = Paths.get(Objects.requireNonNull(uploadPath, "Upload path must be configured"))
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload folder", e);
        }
    }

    private Path getRootPath() {
        if (rootPath == null) {
            init();
        }
        return rootPath;
    }

    @Override
    public void save(MultipartFile file) {
        Path root = getRootPath();
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename(), "File name must not be null");
        Path destination = root.resolve(StringUtils.cleanPath(originalFilename)).normalize();
        if (!destination.startsWith(root)) {
            throw new IllegalArgumentException("Cannot store file outside of the upload directory");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not store the file: " + originalFilename, e);
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path root = getRootPath();
            Path file = root.resolve(filename).normalize();
            if (!file.startsWith(root)) {
                throw new IllegalArgumentException("Cannot read file outside of the upload directory");
            }
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new IllegalStateException("Could not read the file");
            }
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Error while reading the file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        Path root = getRootPath();
        try {
            FileSystemUtils.deleteRecursively(root.toFile());
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new IllegalStateException("Could not delete uploaded files", e);
        }
    }

    public List<Path> loadAll() {
        Path root = getRootPath();
        if (Files.notExists(root)) {
            return Collections.emptyList();
        }
        try (Stream<Path> stream = Files.walk(root, 1)) {
            return stream
                    .filter(path -> !path.equals(root))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Could not list the files", e);
        }
    }
}
