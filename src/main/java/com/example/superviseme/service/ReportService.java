package com.example.superviseme.service;

import com.example.superviseme.record.WeeklySubmission;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReportService {
    private final StudentChapterService studentChapterService;

    public ReportService(StudentChapterService studentChapterService){
        this.studentChapterService = studentChapterService;
    }

    public ResponseEntity<?> getUserGraphData(String userId) {
        Map<String, Map<String, Integer>> submissionsPerMonth = studentChapterService.findPermissionPerMonth(userId);
        List<WeeklySubmission> submissionsPerWeek = studentChapterService.findSubmissionsPerWeek(userId);

        Map<String, Object> combined = new HashMap<>();
        combined.put("monthly", submissionsPerMonth);
        combined.put("weekly", submissionsPerWeek);
        return ResponseEntity.ok(combined);
    }
}
