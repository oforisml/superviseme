package com.example.superviseme.restcontrollers;

import com.example.superviseme.service.StudentChapterService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentChapterController {
    private final StudentChapterService service;

    public StudentChapterController(StudentChapterService service) {
        this.service = service;
    }

//    todo: Creating a student chapter


//    todo: update Student chapter

//    todo: Get student chapter


}
