package com.example.superviseme.repository;

import com.example.superviseme.entities.StudentChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentChapterRepository extends JpaRepository<StudentChapter, UUID> {
}
