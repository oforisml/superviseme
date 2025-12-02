package com.example.superviseme.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity(name = "tbl_comment")
@Table
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonBackReference
    @JoinColumn(name = "submission_id", referencedColumnName = "id")
    private Submission submission;

}
