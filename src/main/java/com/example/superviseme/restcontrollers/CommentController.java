package com.example.superviseme.restcontrollers;

import com.example.superviseme.service.CommentService;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/comments")
@Tag(name = "Comments Controller",
        description = "The endpoints herein are for the CRUD that would be used for comments management")

public class CommentController {
    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getComments(){
        return service.getAllComments();
    }
    
    @GetMapping(value = "/{submissionId}")
    public ResponseEntity<?> getRecentComments(@PathVariable String submissionId){
    	return service.getRecentComments(UUID.fromString(submissionId));
    }
}
