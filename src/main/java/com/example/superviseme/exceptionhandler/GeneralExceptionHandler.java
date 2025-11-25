package com.example.superviseme.exceptionhandler;

import com.example.superviseme.record.ResponseRecord;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Hidden
@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseRecord> handleIllegalArgumentExceptions(IllegalArgumentException exception){
        return new ResponseEntity<>(
                new ResponseRecord(400, exception.getMessage(), null, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseRecord> handleNoFoundException(ResourceNotFoundException exception){
        return new ResponseEntity<>(
                new ResponseRecord(404, exception.getMessage(), null, LocalDateTime.now()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseRecord> handleGeneralException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity<>(
                new ResponseRecord(500, exception.getMessage(), null, LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
