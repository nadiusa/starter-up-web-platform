package com.example.license.exception;

public class APIRequstException extends RuntimeException {
    public APIRequstException(String message) {
        super(message);
    }

    public APIRequstException(String message, Throwable cause) {
        super(message, cause);
    }
}
