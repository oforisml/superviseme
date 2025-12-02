package com.example.superviseme.restcontrollers;

import com.example.superviseme.service.StudentChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/student-chapter/student/")
@CrossOrigin(origins = "*")

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


}
