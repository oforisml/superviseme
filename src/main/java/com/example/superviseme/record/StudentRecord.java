package com.example.superviseme.record;

import com.example.superviseme.entities.StudentProfile;
import com.example.superviseme.enums.Role;

import java.util.UUID;

public record StudentRecord(
        UUID id,
        String studentId,
        Role role,
        StudentProfile studentProfile,
        boolean isUserCreated ,
        boolean isUserProfileCreated,
        boolean isUserProfileActive
) {
}
