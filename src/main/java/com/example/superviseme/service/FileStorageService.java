package com.example.superviseme.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path storagePath;

    @PostConstruct
    public void init() throws IOException {
        storagePath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(storagePath); // Create folder if not existing
    }

    public String saveFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        if (filename.contains("..")) {
            throw new RuntimeException("Invalid file name: " + filename);
        }

        Path target = storagePath.resolve(filename);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    public Path load(String filename) {
        return storagePath.resolve(filename).normalize();
    }

    public Stream<Path> loadAll() throws IOException {
        return Files.walk(storagePath, 1)
                .filter(path -> !path.equals(storagePath))
                .map(storagePath::relativize);
    }

    public boolean delete(String filename) throws IOException {
        return Files.deleteIfExists(load(filename));
    }
}
