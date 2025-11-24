package com.example.superviseme.record;

import com.example.superviseme.enums.SubmissionStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record SubmissionRecord(
        UUID id,
        UUID studentChapterId,
        String summary,
        String comment,
        MultipartFile file,
        SubmissionStatus status
        ) {}
