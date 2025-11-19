package com.example.superviseme.restcontrollers;

import com.example.superviseme.entities.User;
import com.example.superviseme.record.StudentRegistrationDto;
import com.example.superviseme.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(name = "User Controller", value = "/users")

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/")
    public ResponseEntity<User> saveUser(@RequestBody StudentRegistrationDto request){
        userService.intializeStudentCreation(request);
    }

}
