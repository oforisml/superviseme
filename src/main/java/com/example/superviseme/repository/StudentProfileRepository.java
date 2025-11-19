package com.example.superviseme.repository;

import com.example.superviseme.entities.StudentProfile;
import com.example.superviseme.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, UUID> {
    @Query("select (count(s) > 0) from StudentProfile s where s.studentId = ?1")
    boolean existsByStudentIdIs(String studentId);

    @Query("select s from StudentProfile s where s.studentId = ?1")
    Optional<StudentProfile> findByStudentIdIs(String studentId);
    @Query("select s from StudentProfile s where s.user = ?1")
    Optional<StudentProfile> findByUserIs(User user);
}
