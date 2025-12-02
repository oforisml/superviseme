package com.example.superviseme.entities;


import com.example.superviseme.enums.StudentChapterStatus;
import com.example.superviseme.enums.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tbl_student_chapter")
@Table
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StudentChapter{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    private String studentId;


    @ManyToOne
    @JoinColumn(name = "chapter_id", referencedColumnName = "id")
    private Chapter chapter;

    @OneToMany(mappedBy = "studentChapter", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Submission> submissions;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentChapterStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
