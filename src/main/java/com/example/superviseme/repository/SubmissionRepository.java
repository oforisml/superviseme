package com.example.superviseme.repository;


import com.example.superviseme.entities.Submission;
import com.example.superviseme.enums.SubmissionStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    @Query("SELECT WEEK(s.createdAt) AS week, COUNT(s) AS submissionCount " +
            "FROM tbl_submission s " +
            "JOIN s.studentChapter sc " +
            "WHERE sc.studentId = ?1 " +
            "AND s.createdAt BETWEEN ?2 AND ?3 " +
            "GROUP BY WEEK(s.createdAt) " +
            "ORDER BY  WEEK(s.createdAt)")
    List<Object[]> findSubmissionsByWeekForStudent(String studentId, LocalDateTime startOfMonth, LocalDateTime endOfMonth);
    
    List<Submission> findByStudentChapterIdAndStatus(UUID studentChapterId, SubmissionStatus status);
    
    List<Submission> findByStudentChapterIdOrderByCreatedAtDesc(UUID studentChapterId);

    
    
}
