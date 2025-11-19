package com.example.superviseme.record;

import org.springframework.objenesis.SpringObjenesis;

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
     String lectureAlignment

     ){}
