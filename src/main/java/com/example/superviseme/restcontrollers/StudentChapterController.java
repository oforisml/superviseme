package com.example.superviseme.restcontrollers;

import com.example.superviseme.service.StudentChapterService;

import java.util.UUID;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/student-chapter/student/")

@Tag(name = "Student Chapter Controller",
        description = "The endpoints herein are for the CRUD that would be used for student chapter management")

public class StudentChapterController {
    private final StudentChapterService service;

    public StudentChapterController(StudentChapterService service) {
        this.service = service;
    }

    @GetMapping(value = "{studentid}")
    public ResponseEntity<?> getChapters(@PathVariable(name = "studentid") UUID studentId){
        return service.findAllChaptersByStudentId(studentId);
    }


    @GetMapping
    public ResponseEntity<?> getAll(){
        return service.findAll();
    }
    
    @GetMapping(value = "/{studentId}/current")
    public ResponseEntity<?> getCurrentStage(@PathVariable String studentId){
    	return service.getCurrentStage(studentId);
    }
    
    @GetMapping(value = "/{studentId}/completed")
    public ResponseEntity<?> getCompletedChapters(@PathVariable String studentId){
    	return service.getCompletedChapters(studentId);
    }


}
