package com.pablo9298.kmdb.exception;

// Custom exception to handle resource not found scenarios
public class ResourceNotFoundException extends RuntimeException {

    // Constructor that accepts a custom error message
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
