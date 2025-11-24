package com.example.superviseme.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.superviseme.entities.StudentChapter;

public interface StudentChapterRepository extends JpaRepository<StudentChapter,UUID>{

}
