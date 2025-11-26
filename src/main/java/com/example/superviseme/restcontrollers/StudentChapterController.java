package com.example.superviseme.restcontrollers;

import com.example.superviseme.service.StudentChapterService;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/student-chapter/student/")
public class StudentChapterController {
    private final StudentChapterService service;

    public StudentChapterController(StudentChapterService service) {
        this.service = service;
    }

//    todo: Creating a student chapter


//    todo: update Student chapter

//    todo: Get student chapter
    @GetMapping(value = "{studentid}")
    public ResponseEntity<?> getChapters(@PathVariable(name = "studentid") String studentId){
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
