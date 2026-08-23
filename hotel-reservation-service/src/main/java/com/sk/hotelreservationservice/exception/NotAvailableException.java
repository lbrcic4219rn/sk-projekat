package com.sk.hotelreservationservice.exception;

import org.springframework.http.HttpStatus;

public class NotAvailableException extends CustomException {

    public NotAvailableException(String message) {
        super(message, ErrorCode.NOT_AVAILABLE, HttpStatus.CONFLICT);
    }
}
