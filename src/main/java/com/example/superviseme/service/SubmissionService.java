package com.example.superviseme.service;

import com.example.superviseme.entities.Comment;
import com.example.superviseme.entities.Document;
import com.example.superviseme.entities.StudentChapter;
import com.example.superviseme.entities.Submission;
import com.example.superviseme.enums.SubmissionStatus;
import com.example.superviseme.exceptionhandler.ResourceNotFoundException;
import com.example.superviseme.record.SubmissionRecord;
import com.example.superviseme.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class SubmissionService {
    private final SubmissionRepository repository;
    private final FileStorageService fileStorageService;
    private final CommentService commentService;
    private final StudentChapterService studentChapterService;

    public SubmissionService(SubmissionRepository repository, FileStorageService fileStorageService, CommentService commentService, StudentChapterService studentChapterService) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
        this.commentService = commentService;
        this.studentChapterService = studentChapterService;
    }

    public ResponseEntity<?> save(SubmissionRecord record) throws IOException {

        // Fetch student Chapter
        StudentChapter studentChapter = studentChapterService.findById(record.studentChapterId());


        Submission submission = new Submission();
        submission.setSummary(record.summary());
        submission.setStudentChapter(studentChapter);
        submission.setStatus(SubmissionStatus.PENDING);


        submission = repository.saveAndFlush(submission);

        // Persisting the file
        Document document = fileStorageService.saveFile(record.file());

        document.setSubmission(submission);
        fileStorageService.persistDocument(document);

        submission = repository.findById(submission.getId()).get();

        return ResponseEntity.ok(submission);
    }

    public ResponseEntity<?> update(UUID id, SubmissionRecord record) {
        // validate Id exists
        if(null == id)
            throw new IllegalArgumentException("Submission id cannot be null");

        Submission submission = repository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Submission not found for id: "+ id));

        if(record.status() != null )
            submission.setStatus(record.status());

        submission = repository.save(submission);

        if(record.comment() != null){
            Comment comment = new Comment();
            comment.setComment(record.comment());
            comment.setSubmission(submission);
            comment = commentService.persistComment(comment);

            System.out.println(comment.getComment());
        }

        submission = repository.findById(id).get();
        return ResponseEntity.ok(submission);

    }
}
