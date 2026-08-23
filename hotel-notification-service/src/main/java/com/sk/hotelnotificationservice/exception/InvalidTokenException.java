package com.sk.hotelnotificationservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends CustomException {

    public InvalidTokenException(String message) {
        super(message, ErrorCode.INVALID_TOKEN, HttpStatus.UNAUTHORIZED);
    }
}
