package com.example.superviseme.restcontrollers;


import com.example.superviseme.record.SubmissionRecord;
import com.example.superviseme.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/submissions")
public class SubmissionController {
    private final SubmissionService service;

    public SubmissionController(SubmissionService service) {
        this.service = service;
    }


    @PostMapping(value = "/")
    public ResponseEntity<?> saveSubmission(@RequestBody SubmissionRecord record) throws IOException {
        return service.save(record);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateSubmission(@PathVariable(name = "id")UUID id, SubmissionRecord record){
        return service.update(id, record);
    }
}
