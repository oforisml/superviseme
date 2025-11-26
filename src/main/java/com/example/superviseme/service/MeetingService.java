package com.example.superviseme.service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.superviseme.entities.Meeting;
import com.example.superviseme.enums.MeetingDuration;
import com.example.superviseme.enums.MeetingType;
import com.example.superviseme.record.MeetingRecord;
import com.example.superviseme.repository.MeetingRepository;


@Service
public class MeetingService {
	
	private final MeetingRepository meetingRepository;
	
	MeetingService(MeetingRepository meetingRepository) {
		this.meetingRepository = meetingRepository;
	}
	
	public ResponseEntity<?> createMeeting(MeetingRecord meetingDto) {
		if(meetingDto != null) {
			Meeting meeting = new Meeting();
			
			if(meetingDto.date() != null && meetingDto.time() != null) {
				meeting.setMeetingStart(meetingDto.date().atTime(meetingDto.time()));
			}
			
			MeetingType meetingType = MeetingType.values()[meetingDto.meetingType()];
				switch (meetingType) {
				case ONLINE -> meeting.setLink(meetingDto.link());
				case IN_PERSON -> meeting.setLocation(meetingDto.location());
				default -> throw new IllegalArgumentException("Unexpected value: " + meetingType);
				}
			
			meeting.setMeetingType(meetingType);	
				
		    if(meetingDto.agenda() != null) {
		    	meeting.setAgenda(meetingDto.agenda());
		    }
		    
		    MeetingDuration duration = MeetingDuration.values()[meetingDto.duration()];
		    meeting.setMeetingDuration(duration);
		
		    if(isAvailable(meeting.getMeetingStart(), meeting.getMeetingDuration().getDuration())) {
		    	meeting = meetingRepository.save(meeting);
			    return ResponseEntity.ok(meeting);
		    }else {
		    	return ResponseEntity.ok("Meeting slot is booked, Try another time");
		    }
		    
		    
		}
		return ResponseEntity.ofNullable(null);		
		
	}
	
	public ResponseEntity<?> getPendingMeetings(){
		List<Meeting> meeting =  meetingRepository.findByMeetingStartAfter(LocalDateTime.now());
		if(!meeting.isEmpty()) {
			return ResponseEntity.ok(meeting.size());
		}
		return ResponseEntity.ok(0);
	}
	
	private boolean overlaps(Meeting meeting,LocalDateTime newStart, Duration newDuration) {
        LocalDateTime newEnd = newStart.plus(newDuration);
        return !newEnd.isBefore(meeting.getMeetingStart()) && !newStart.isAfter(meeting.getMeetingStart().plus(meeting.getMeetingDuration().getDuration()));
    }
	
	public boolean isAvailable(LocalDateTime start, Duration duration) {
		List<Meeting> meetings = meetingRepository.findAll();
        for (Meeting meeting : meetings) {
            if (overlaps(meeting,start, duration)) {
                return false;
            }
        }
        return true;
    }
	

}
