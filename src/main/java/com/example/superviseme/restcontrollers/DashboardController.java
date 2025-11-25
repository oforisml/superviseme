package com.example.superviseme.restcontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    @GetMapping("{id}")
    public ResponseEntity<?> getUserDashboard(@PathVariable(name = "id") UUID userId){
        return null;
    }
}
