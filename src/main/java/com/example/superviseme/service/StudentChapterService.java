package com.example.superviseme.service;

import com.example.superviseme.entities.StudentChapter;
import com.example.superviseme.repository.StudentChapterRepository;
import com.example.superviseme.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StudentChapterService {
    private final StudentChapterRepository repository;
    private final StudentProfileRepository studentProfileRepository;

    public StudentChapterService(StudentChapterRepository repository,
                                 StudentProfileRepository studentProfileRepository) {
        this.repository = repository;
        this.studentProfileRepository = studentProfileRepository;
    }


    public StudentChapter findById(UUID studentChapterId) {
        return repository.findById(studentChapterId).orElse(new StudentChapter());
    }


}
