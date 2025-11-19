package com.example.superviseme.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Table
@Entity(name = "tbl_student_profile")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String programType;
    @Column(name = "student_id", unique = true)
    private String studentId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "research_area")
    private String researchArea;

    @Column(name = "research_topic")
    private String researchTopic;

    @Override
    public String toString() {
        return "StudentProfile{" +
                "id=" + id +
                ", user=" + user +
                ", programType='" + programType + '\'' +
                ", studentId='" + studentId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", researchArea='" + researchArea + '\'' +
                ", researchTopic='" + researchTopic + '\'' +
                ", abstractText='" + abstractText + '\'' +
                ", researchObjectives='" + researchObjectives + '\'' +
                ", lectureAlignment='" + lectureAlignment + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Column
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getResearchArea() {
        return researchArea;
    }

    public void setResearchArea(String researchArea) {
        this.researchArea = researchArea;
    }

    public String getResearchTopic() {
        return researchTopic;
    }

    public void setResearchTopic(String researchTopic) {
        this.researchTopic = researchTopic;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getResearchObjectives() {
        return researchObjectives;
    }

    public void setResearchObjectives(String researchObjectives) {
        this.researchObjectives = researchObjectives;
    }

    public String getLectureAlignment() {
        return lectureAlignment;
    }

    public void setLectureAlignment(String lectureAlignment) {
        this.lectureAlignment = lectureAlignment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}