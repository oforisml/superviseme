package com.example.superviseme.repository;

import com.example.superviseme.entities.StudentChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import com.example.superviseme.enums.StudentChapterStatus;
import com.example.superviseme.entities.Chapter;



@Repository
public interface StudentChapterRepository extends JpaRepository<StudentChapter, UUID> {
    @Query("select t from tbl_student_chapter t where t.studentId = ?1 order by t.createdAt")
    List<StudentChapter> findByStudentIdOrderByCreatedAtAsc(String studentId);
    
    List<StudentChapter> findByStudentIdAndStatus(String studentId, StudentChapterStatus status);
    
    


}
