package com.example.superviseme.record;

import com.example.superviseme.entities.User;

import java.util.UUID;

public record StudentLoginCheck(
        boolean isStudentCreated,
        boolean isStudentProfileCreated,
        boolean isStudentProfileActive,
        User user
) {
}
