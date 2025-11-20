package com.example.superviseme.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.superviseme.enums.MeetingDuration;
import com.example.superviseme.enums.MeetingType;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "tbl_meeting")
@Table
@Data
public class Meeting {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
	
	@Column(nullable = false)
	private LocalDateTime meetingStart;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MeetingDuration meetingDuration;
	
	private String location;
	private String link;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MeetingType meetingType;
	
	@Column(nullable = false)
	private String agenda;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

}
