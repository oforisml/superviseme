package com.example.superviseme.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Entity(name = "tbl_student_chapter")
@Table
@Data
public class StudentChapter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String studentId;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Submission> submissions;

}
