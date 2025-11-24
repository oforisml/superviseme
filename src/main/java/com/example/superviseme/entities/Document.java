package com.example.superviseme.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tbl_resource")
@Table
@Data
public class Document {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private UUID id;
    private String fileType;
    private String fileName;

    private String generatedFileName;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
