package com.example.superviseme.service;

import com.example.superviseme.repository.DocumentRepository;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }
}
