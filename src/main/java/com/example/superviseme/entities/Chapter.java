package com.example.superviseme.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tbl_chapter")
@Table
@Data
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    private String name; //Added this to know which particular chapter we are working on

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "next_chapter_id", referencedColumnName = "id")
    private Chapter nextChapter;

    @JsonIgnore
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<StudentChapter> submissions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
