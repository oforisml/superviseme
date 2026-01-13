package com.example.superviseme.restcontrollers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.superviseme.record.MeetingRecord;
import com.example.superviseme.service.MeetingService;

@RestController
@RequestMapping(name="Meeting Controller", value = "/meetings")

@Tag(name = "Meeting Controller",
		description = "The endpoints herein are for the CRUD that would be used for meeting management")

public class MeetingController {
	
	private final MeetingService meetingService;
	
	public MeetingController(MeetingService meetingService) {
		this.meetingService = meetingService;
	}
	
	@PostMapping(value = "/")
    public ResponseEntity<?> saveMeeting(@RequestBody MeetingRecord meetingDto){
        return meetingService.createMeeting(meetingDto);
    }
	
	@GetMapping(value = "/pending")
	public ResponseEntity<?> getPendingMeetings(){
		return meetingService.getPendingMeetings();
	}

}
