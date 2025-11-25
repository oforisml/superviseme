package com.example.superviseme.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tbl_resource")
@Table
@Data
public class Document implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private UUID id;
    private String fileType;
    private String fileName;

    private String generatedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonBackReference
    @JoinColumn(name = "submission_id", referencedColumnName = "id")
    private Submission submission;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
