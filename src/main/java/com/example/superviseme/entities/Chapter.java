package com.example.superviseme.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tbl_chapter")
@Table
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name; //Added this to know which particular chapter we are working on

    private UUID nextChapterId;
    
    @Column(nullable = false)
    @ColumnDefault("1")
    private int stage;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private Set<StudentChapter> studentChapters;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
