package com.sk.hotelreservationservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidReservationException extends CustomException {

    public InvalidReservationException(String message) {
        super(message, ErrorCode.INVALID_RESERVATION, HttpStatus.BAD_REQUEST);
    }
}
