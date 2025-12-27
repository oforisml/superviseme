package com.example.superviseme.service;

import com.example.superviseme.entities.StudentChapter;
import com.example.superviseme.entities.Submission;
import com.example.superviseme.entities.User;
import com.example.superviseme.enums.StudentChapterStatus;
import com.example.superviseme.enums.SubmissionStatus;
import com.example.superviseme.exceptionhandler.ResourceNotFoundException;
import com.example.superviseme.record.WeeklySubmission;
import com.example.superviseme.repository.ChapterRepository;
import com.example.superviseme.repository.StudentChapterRepository;
import com.example.superviseme.repository.SubmissionRepository;
import com.example.superviseme.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {
    private final StudentChapterService studentChapterService;
    private final UserRepository userRepository;
    private final ChapterRepository chapterRepository;
    private final StudentChapterRepository studentChapterRepository;
    private final SubmissionRepository submissionRepository;

    public ReportService(StudentChapterService studentChapterService,
                         UserRepository userRepository,
                         ChapterRepository chapterRepository,
                         StudentChapterRepository studentChapterRepository,
                         SubmissionRepository submissionRepository){
        this.studentChapterService = studentChapterService;
        this.userRepository = userRepository;
        this.chapterRepository = chapterRepository;
        this.studentChapterRepository = studentChapterRepository;
        this.submissionRepository = submissionRepository;
    }

    public ResponseEntity<?> getUserGraphData(UUID userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Map<String, Map<String, Integer>> submissionsPerMonth = studentChapterService.findPermissionPerMonth(user.getStudentId());
        List<WeeklySubmission> submissionsPerWeek = studentChapterService.findSubmissionsPerWeek(user.getStudentId());
        long totalChapters = chapterRepository.count();
        long totalChaptersCompleted = studentChapterRepository.countByStudentIdEqualsIgnoreCaseAndChapter_StudentChapters_StatusEquals(user.getStudentId(), StudentChapterStatus.CLOSED);
        long totalSubmittedForStudentChapter = studentChapterRepository.countByStudentIdEqualsAndSubmissionsNotEmpty(user.getStudentId());
        long totalPendingReview = submissionRepository.countByStudentChapter_StudentIdEqualsIgnoreCaseAndStatusEquals(user.getStudentId(), SubmissionStatus.ACCEPTED);

        StudentChapter studentChapter = studentChapterRepository.findByStudentIdEqualsIgnoreCaseAndStatusEquals(user.getStudentId(), StudentChapterStatus.OPENED);

        List<Map<String, String>> recents = studentChapterService.getRecentSubmissions(user.getStudentId());


        String program = user.getStudentProfile().getProgramType();
        String researchArea = user.getStudentProfile().getResearchArea();
        String names[] = user.getStudentProfile().getFullName().split(" ");
        String lastName = names[names.length -1];

        String abbreviation = "";
        for(String name : names)
            abbreviation+=name.substring(0,1).toUpperCase();


        Map<String, Object> combined = new HashMap<>();
        combined.put("monthly", submissionsPerMonth);
        combined.put("weekly", submissionsPerWeek);
        combined.put("totalChapters", totalChapters);
        combined.put("totalChaptersCompleted", totalChaptersCompleted );
        combined.put("totalSubmittedForStudentChapter", totalSubmittedForStudentChapter );
        combined.put("totalPendingReview", totalPendingReview);
        combined.put("currentChapter", studentChapter!=null ? studentChapter.getChapter().getName().toUpperCase() : "-");
        combined.put("program", program.toUpperCase());
        combined.put("researchArea", researchArea);
        combined.put("lastName", lastName);
        combined.put("abbreviation", abbreviation);
        combined.put("recents", recents);


        return ResponseEntity.ok(combined);
    }
}
