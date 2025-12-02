package com.example.superviseme.repository;

import com.example.superviseme.entities.StudentChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentChapterRepository extends JpaRepository<StudentChapter, UUID> {
    @Query("select t from tbl_student_chapter t where upper(t.studentId) = upper(?1) order by t.createdAt")
    Optional<StudentChapter> findByStudentIdEqualsAllIgnoreCaseOrderByCreatedAtAsc(String studentId);
    Optional<StudentChapter> findFirstBy();
    @Query("""
            select count(t) from tbl_student_chapter t inner join t.submissions submissions
            where t.studentId = ?1 and submissions.createdAt between ?2 and ?3""")
    long countByStudentIdEqualsAndSubmissions_CreatedAtBetween(String studentId, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);
    @Query("select t from tbl_student_chapter t where t.studentId = ?1 order by t.createdAt")
    List<StudentChapter> findByStudentIdOrderByCreatedAtAsc(String studentId);


}
