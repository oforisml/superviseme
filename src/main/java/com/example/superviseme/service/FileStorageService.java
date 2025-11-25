package com.example.superviseme.service;

import com.example.superviseme.entities.Document;
import com.example.superviseme.exceptionhandler.FileStorageException;
import com.example.superviseme.exceptionhandler.ResourceNotFoundException;
import com.example.superviseme.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
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
        if (file.isEmpty()) {
            throw new RuntimeException("No file provided");
        }

        String generatedFileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (generatedFileName.contains("..")) {
            throw new RuntimeException("Invalid file name: " + generatedFileName);
        }

        // Generate unique filename with timestamp and UUID
        String fileName = generatedFileName.split("\\.")[0];
        String fileType = "UNKNOWN";
        String[] fileParts = generatedFileName.split("\\.");
        if (fileParts.length > 1) {
            fileType = fileParts[1].toUpperCase();
        }

        generatedFileName = String.format("%s_%s_%s", fileName, LocalDateTime.now(), UUID.randomUUID());

        Path target = storagePath.resolve(generatedFileName);

        // Ensure the directory exists
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        // Copy the file
        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + generatedFileName);
        }

        // Persisting in the db
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

    public ResponseEntity<Resource> download(UUID id) throws MalformedURLException {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        String filename= String.format("%s.%s", document.getFileName(), document.getFileType().toLowerCase()) ;
        String generatedFileName = document.getGeneratedFileName();

        Path path = load(generatedFileName);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    public List<Document> findAll() {
        List<Document> documents = documentRepository.findAll();
        return documents;
    }

    public List<Document> findAllBySubmissionId(UUID id) {
        return documentRepository.findBySubmission_IdEquals(id);
    }
}
