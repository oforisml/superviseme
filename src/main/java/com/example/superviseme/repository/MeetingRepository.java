package com.example.superviseme.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.superviseme.entities.Meeting;
import java.time.LocalDateTime;


@Repository
public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
	
	List<Meeting> findByMeetingStartAfter(LocalDateTime meetingDate);

}
