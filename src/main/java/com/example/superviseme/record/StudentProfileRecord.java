package com.example.superviseme.record;

import java.util.UUID;
public record StudentRecord(
     UUID id,
     String email,
     String pin,
     String studentId,
     String phoneNumber,
     Boolean isActive,
     UUID studentProfileId,

     String fullName,
     String thesisTitle,
     String abstractText,

     String programType,
     String researchArea,
     String researchObjective

     )
    
{}
