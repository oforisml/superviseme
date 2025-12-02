package com.example.superviseme.record;

import java.time.LocalDate;
import java.time.LocalTime;

public record MeetingRecord(
		LocalDate date,
		LocalTime time,
		String location,
		String link,
		String agenda,
		int duration,
		int meetingType,
		String studentId
		) {

}
