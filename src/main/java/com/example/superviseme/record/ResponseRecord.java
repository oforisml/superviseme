package com.example.superviseme.record;


import java.time.LocalDateTime;

public record ResponseRecord (
        int statusCode,
        String message,
        Object data,
        LocalDateTime timestamp

){
}
