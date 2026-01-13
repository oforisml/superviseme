package com.example.superviseme.restcontrollers;


import com.example.superviseme.enums.SubmissionStatus;
import com.example.superviseme.record.SubmissionRecord;
import com.example.superviseme.service.SubmissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/submissions")

@Tag(name = "Submission Controller",
        description = "The endpoints herein are for the CRUD that would be used for submissions management")

public class SubmissionController {
    private final SubmissionService service;

    public SubmissionController(SubmissionService service) {
        this.service = service;
    }


    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveSubmission(
            @RequestParam(value = "id", required = false) UUID id,
            @RequestParam(value = "studentChapterId") UUID studentChapterId,
            @RequestParam(value = "summary", required = false) String summary,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam(value = "file", required = false)  MultipartFile file,
            @RequestParam(value = "status", required = false) SubmissionStatus status) throws IOException {

        SubmissionRecord record = new SubmissionRecord(id, studentChapterId, summary, comment, file, status);
        return service.save(record);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateSubmission(@PathVariable(name = "id")UUID id, SubmissionRecord record){
        return service.update(id, record);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSubmission(@PathVariable(name = "id")UUID id){
        return service.getSubmission(id);
    }
    
    @GetMapping(value = "/{studentChapterId}/pending")
    public ResponseEntity<?> getPendingSubmissions(@PathVariable String studentChapterId ){
        return service.getPendingSubmissions(UUID.fromString(studentChapterId));
    }
    
    @GetMapping(value = "/{studentChapterId}/recent")
    public ResponseEntity<?> getRecentSubmissions(@PathVariable String studentChapterId ){
        return service.getRecentSubmissions(UUID.fromString(studentChapterId));
    }

}
