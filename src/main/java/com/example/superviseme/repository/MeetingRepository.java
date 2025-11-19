package com.example.superviseme.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.superviseme.entities.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, UUID> {

}
