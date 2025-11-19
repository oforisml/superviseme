package com.example.superviseme.restcontrollers;

import com.example.superviseme.service.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(name = "Resources Controller", value = "/resource")
@Tag(
        name = "Resources Controller",
        description = "The endpoints herein are for the CRUD of fees that would be used for resource management"
)
@RestController
public class ResourcesController {

    private final FileStorageService fileService;

    public ResourcesController(FileStorageService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = fileService.saveFile(file);
        return ResponseEntity.ok("Uploaded: " + filename);
    }

    @GetMapping
    public ResponseEntity<List<String>> listFiles() throws IOException {
        List<String> files = fileService.loadAll()
                .map(Path::toString)
                .collect(Collectors.toList());
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws MalformedURLException {
        Path path = fileService.load(filename);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) throws IOException {
        boolean deleted = fileService.delete(filename);

        if (deleted)
            return ResponseEntity.ok("Deleted: " + filename);
        return ResponseEntity.badRequest().body("File not found: " + filename);
    }

}
