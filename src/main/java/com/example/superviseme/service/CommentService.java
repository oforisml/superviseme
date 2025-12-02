package com.example.superviseme.service;

import com.example.superviseme.entities.Comment;
import com.example.superviseme.entities.Submission;
import com.example.superviseme.repository.CommentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<?> getAllComments() {
        List<Comment> comments = repository.findAll();
        return ResponseEntity.ok(comments);
    }

    public Comment save(String msg, Submission submission) {
        Comment comment = new Comment();
        comment.setComment(msg);
        comment.setSubmission(submission);
        return persistComment(comment);
    }

    public Comment persistComment(Comment comment){
        return repository.save(comment);
    }

    public List<Comment> findAllBySubmissionId(UUID id) {
        return repository.findBySubmission_IdEquals(id);
    }
    
    public ResponseEntity<?> getRecentComments(UUID submissionId){
    	List<Comment> comments = repository.findBySubmissionIdOrderByCreatedAtDesc(submissionId);
    	return ResponseEntity.ok(comments);
    }
}
