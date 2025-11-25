package com.example.superviseme.restcontrollers;

import com.example.superviseme.entities.Document;
import com.example.superviseme.exceptionhandler.ResourceNotFoundException;
import com.example.superviseme.service.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping(name = "Resources Controller", value = "/resource")
@Tag(
        name = "Resources Controller",
        description = "The endpoints herein are for the CRUD of fees that would be used for resource management"
)
@RestController
public class ResourcesController {

    private final FileStorageService fileService;

    public ResourcesController( FileStorageService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Document document = fileService.saveFile(file);
        return ResponseEntity.ok(document);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> listFiles() throws IOException {

        List<Document> documents = fileService.findAll();
        return ResponseEntity.ok(documents);
    }


    @GetMapping(value = "/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable(name = "id") UUID id)
            throws MalformedURLException {
        return fileService.download(id);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getFileById(@PathVariable(name = "id") UUID id) {
        Document files = fileService.findById(id);
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) throws IOException {
        boolean deleted = fileService.delete(filename);

        if (deleted)
            return ResponseEntity.ok("Deleted: " + filename);
        throw new ResourceNotFoundException("File not found");
    }


}
