package com.example.superviseme.record;

import com.example.superviseme.entities.User;
import com.example.superviseme.enums.Role;

import java.util.UUID;
public record StudentProfileRecord(
     UUID id,
     String email,
     String pin,
     String studentId,
     String phoneNumber,
     Boolean isActive,
     String fullName,
     String thesisTitle,
     String abstractText,
     String programType,
     String researchArea,
     String researchObjective,
     String lectureAlignment,
     Role role

     ){}
