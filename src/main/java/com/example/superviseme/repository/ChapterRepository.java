package com.example.superviseme.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.superviseme.entities.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, UUID>{

}
