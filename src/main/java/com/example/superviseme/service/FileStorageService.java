package com.example.superviseme.service;

import com.example.superviseme.entities.Document;
import com.example.superviseme.exceptionhandler.ResourceNotFoundException;
import com.example.superviseme.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path storagePath;
    private final DocumentRepository documentRepository;

    public FileStorageService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @PostConstruct
    public void init() throws IOException {
        storagePath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(storagePath); // Create folder if not existing
    }

    public Document saveFile(MultipartFile file) throws IOException {

        String generatedFileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (generatedFileName.contains("..")) {
            throw new RuntimeException("Invalid file name: " + generatedFileName);
        }
        Path target = storagePath.resolve(generatedFileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // Persisting in the db
        String originalFileName = file.getOriginalFilename();
        String fileType = (originalFileName.split(".")[1]).toUpperCase();
        String fileName = originalFileName.split(".")[0];

        Document document = new Document();
        document.setFileName(fileName);
        document.setFileType(fileType);
        document.setGeneratedFileName(generatedFileName);

        document = documentRepository.save(document);

        return document;
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


    public Document findById(UUID id) {
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if(optionalDocument.isPresent()){
        }else{
            throw new ResourceNotFoundException("Document not found for id: "+id);
        }

        return  optionalDocument.get();
    }


    public Document persistDocument(Document document){
        return documentRepository.save(document);

    }
}
