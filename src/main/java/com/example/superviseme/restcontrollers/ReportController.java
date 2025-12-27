package com.example.superviseme.restcontrollers;

import com.example.superviseme.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getUserReport(@PathVariable(name = "id") UUID userId){
        return reportService.getUserGraphData(userId);
    }
}
