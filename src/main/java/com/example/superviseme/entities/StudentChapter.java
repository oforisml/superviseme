package com.example.superviseme.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tbl_student_chapter")
@Table
@Data
public class StudentChapter{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String studentId;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @OneToMany(mappedBy = "studentChapter", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Submission> submissions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
