package com.example.superviseme.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.UUID;

@Entity
@Data
public class StudentProfile {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "student_id", unique = true)
    private String studentId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "research_area")
    private String researchArea;

    @Column(name = "research_topic")
    private String researchTopic;

    @Column(name = "thesis_title")
    private String thesisTitle;

    @Column(length = 2000)
    private String abstractText;

    @Column(name = "research_objectives", length = 1000)
    private String researchObjectives;

    @Column(name = "enrollment_date")
    private java.time.LocalDate enrollmentDate;

    @Column(name = "expected_graduation_date")
    private java.time.LocalDate expectedGraduationDate;

    @Column(name = "current_semester")
    private Integer currentSemester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private SupervisorProfile supervisor;

    @CreationTimestamp
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
}