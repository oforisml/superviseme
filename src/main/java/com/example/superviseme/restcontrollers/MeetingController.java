package com.example.superviseme.restcontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.superviseme.record.MeetingRecord;
import com.example.superviseme.service.MeetingService;

@RestController
@RequestMapping(name="Meeting Controller", value = "/meetings")
@CrossOrigin(origins = "*")

public class MeetingController {
	
	private final MeetingService meetingService;
	
	public MeetingController(MeetingService meetingService) {
		this.meetingService = meetingService;
	}
	
	@PostMapping(value = "/")
    public ResponseEntity<?> saveMeeting(@RequestBody MeetingRecord meetingDto){
        return meetingService.createMeeting(meetingDto);
    }

}
