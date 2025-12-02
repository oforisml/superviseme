package com.example.superviseme.entities;

import com.example.superviseme.enums.MeetingDuration;
import com.example.superviseme.enums.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;


@Entity(name = "tbl_submission")
@Table
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Submission  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    private String summary;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @OneToMany(mappedBy = "submission", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonManagedReference
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "submission", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonManagedReference
    private Set<Document> resources = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "student_chapter_id", referencedColumnName = "id")
    private StudentChapter studentChapter;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
