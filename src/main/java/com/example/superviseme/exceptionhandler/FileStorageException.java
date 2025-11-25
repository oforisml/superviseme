package com.example.superviseme.exceptionhandler;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }
}