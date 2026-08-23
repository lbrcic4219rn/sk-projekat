package com.sk.hotelreservationservice.exception;

import org.springframework.http.HttpStatus;

public class UserServiceUnavailableException extends CustomException {

    public UserServiceUnavailableException(String message) {
        super(message, ErrorCode.USER_SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
