package com.example.superviseme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.UUID;

@Table
@Entity(name = "tbl_student_profile")
@Data
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String programType;
    @Column(name = "student_id", unique = true)
    private String studentId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "research_area")
    private String researchArea;

    @Column(name = "research_topic")
    private String researchTopic;


    @Column(length = 5000)
    private String abstractText;

    @Column(name = "research_objectives")
    private String researchObjectives;

    @Column(name = "lecturer_alignment")
    private String lectureAlignment;

    @CreationTimestamp
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

}