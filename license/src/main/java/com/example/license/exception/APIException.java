package com.example.license.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class APIException {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timpstamp;

    public APIException(String message, HttpStatus httpStatus, ZonedDateTime timpstamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timpstamp = timpstamp;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimpstamp() {
        return timpstamp;
    }
}
