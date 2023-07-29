package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import sorim.f1.slasher.relentless.model.FileData;
import sorim.f1.slasher.relentless.model.UploadResponseMessage;
import sorim.f1.slasher.relentless.service.FileService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("files")
public class FilesController {

    private final SecurityService securityService;
    private final FileService fileService;

    @PostMapping
    public ResponseEntity<UploadResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            fileService.save(file);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new UploadResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new UploadResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!"));
        }
    }

    @DeleteMapping
    public void delete() {
        fileService.deleteAll();
    }

    private FileData pathToFileData(Path path) {
        FileData fileData = new FileData();
        String filename = path.getFileName()
                .toString();
        fileData.setFilename(filename);
        fileData.setUrl(MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", filename)
                .build()
                .toString());
        try {
            fileData.setSize(Files.size(path));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }

        return fileData;
    }

    @GetMapping("{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = fileService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
