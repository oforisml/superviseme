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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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



        // Persisting the file
        Document document = fileStorageService.saveFile(record.file());

        submission.getResources().add(document);

        submission = repository.saveAndFlush(submission);

        return ResponseEntity.ok(submission);
    }


    public ResponseEntity<?> update(UUID id, SubmissionRecord record) {
        // validate Id exists
        if(null == id)
            throw new IllegalArgumentException("Submission id cannot be null");

        Submission submission = repository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Submission not found for id: "+ id));

        if(record.status() != null ){
            submission.setStatus(record.status());
            if(record.status().equals(SubmissionStatus.ACCEPTED)){
                studentChapterService.advanceStudentChapter(submission.getStudentChapter());
            }

        }

        submission = repository.save(submission);

        if(record.comment() != null){
            Comment comment = new Comment();
            comment.setComment(record.comment());
            comment = commentService.persistComment(comment);

            submission.getComments().add(comment);

            submission = repository.save(submission);

            System.out.println(comment.getComment());
        }


        return ResponseEntity.ok(submission);

    }

    public ResponseEntity<?> getSubmission(UUID id) {
        Submission submission = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Submission not found"));

        // Add to submission
        Set<Document> documents = new HashSet<>(fileStorageService.findAllBySubmissionId(submission.getId()));
        submission.setResources(documents);

        // Add comments to submission
        Set<Comment> comments = new HashSet<>(commentService.findAllBySubmissionId(id));
        submission.setComments(comments);

        return ResponseEntity.ok(submission);
    }
}
