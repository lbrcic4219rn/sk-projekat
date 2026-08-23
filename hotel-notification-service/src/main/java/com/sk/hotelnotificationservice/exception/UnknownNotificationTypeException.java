package com.sk.hotelnotificationservice.exception;

import org.springframework.http.HttpStatus;

public class UnknownNotificationTypeException extends CustomException {

    public UnknownNotificationTypeException(String message) {
        super(message, ErrorCode.UNKNOWN_NOTIFICATION_TYPE, HttpStatus.BAD_REQUEST);
    }
}
