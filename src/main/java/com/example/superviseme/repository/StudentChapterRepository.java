package com.example.superviseme.repository;

import com.example.superviseme.entities.StudentChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.example.superviseme.enums.StudentChapterStatus;


@Repository
public interface StudentChapterRepository extends JpaRepository<StudentChapter, UUID> {
    @Query("select t from tbl_student_chapter t where upper(t.studentId) = upper(?1) and t.submissions is not empty order by t.createdAt desc")
    List<StudentChapter> findByStudentIdEqualsIgnoreCase(String studentId);
    @Query("select t from tbl_student_chapter t where upper(t.studentId) = upper(?1) and t.status = ?2")
    StudentChapter findByStudentIdEqualsIgnoreCaseAndStatusEquals(String studentId, StudentChapterStatus status);
    @Query("select count(distinct t) from tbl_student_chapter t where t.studentId = ?1 and t.submissions is not empty")
    long countByStudentIdEqualsAndSubmissionsNotEmpty(String studentId);
    @Query("""
            select count(t) from tbl_student_chapter as t where upper(t.studentId) = upper(?1) and  t.status = ?2""")
    long countByStudentIdEqualsIgnoreCaseAndChapter_StudentChapters_StatusEquals(String studentId, StudentChapterStatus status);
    @Query("select (count(t) > 0) from tbl_student_chapter t where upper(t.studentId) = upper(?1)")
    boolean existsByStudentIdEqualsIgnoreCase(String studentId);
    @Query("select t from tbl_student_chapter t where upper(t.studentId) = upper(?1)  order by t.createdAt limit 1")
    Optional<StudentChapter> findByStudentIdEqualsAllIgnoreCaseOrderByCreatedAtAsc(String studentId);
    Optional<StudentChapter> findFirstBy();
    @Query("""
            select count(t) from tbl_student_chapter t inner join t.submissions submissions
            where t.studentId = ?1 and submissions.createdAt between ?2 and ?3""")
    long countByStudentIdEqualsAndSubmissions_CreatedAtBetween(String studentId, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);
//    @Query("select t from tbl_student_chapter t where t.studentId = ?1 order by t.createdAt")
    @Query("select t from tbl_student_chapter t LEFT JOIN  tbl_user u on u.studentId=t.studentId where u.id = ?1 order by t.createdAt")
    List<StudentChapter> findByStudentIdOrderByCreatedAtAsc(UUID studentId);
    
    List<StudentChapter> findByStudentIdAndStatus(String studentId, StudentChapterStatus status);
    
    


}
