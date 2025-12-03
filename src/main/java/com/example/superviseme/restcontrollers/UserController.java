package com.example.superviseme.restcontrollers;

import com.example.superviseme.record.StudentRegistrationDto;
import com.example.superviseme.record.StudentProfileRecord;
import com.example.superviseme.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(name = "User Controller", value = "/users")
@Tag(name = "User Controller",
        description = "The endpoints herein are for the CRUD of fees that would be used for user and profile management")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/")
    public ResponseEntity<?> saveUser(@RequestBody StudentRegistrationDto request){
        return userService.initializeStudentCreation(request);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateProfile(@RequestBody StudentProfileRecord request){
        return userService.updateStudentProfile(request);
    }


    @PutMapping(value = "/{userId}/status")
    public ResponseEntity<?> changeProfileStatus(@PathVariable(name = "userId") UUID userId,
                                               @RequestParam(name = "status") boolean status){
        return updateProfile(new StudentProfileRecord(userId, null, null, null, null, status,
                null, null,null, null, null, null, null, null));
    }



    @GetMapping(value = "/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable(name = "userId") UUID userId){
        return userService.getUser(userId);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<?> deleteMapping(@PathVariable(name = "userId") UUID userId){

        return userService.updateStudentProfile(
                new StudentProfileRecord(
                userId,null, null, null, null,
                false, null, null,null,
                        null,null,null, null, null)
        );
    }
}
