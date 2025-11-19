package com.example.superviseme.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.superviseme.enums.MeetingDuration;
import com.example.superviseme.enums.MeetingType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
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
	
	

}
