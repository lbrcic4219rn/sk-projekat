package com.sk.hotelnotificationservice.exception;

import org.springframework.http.HttpStatus;

public class EmailDeliveryException extends CustomException {

    public EmailDeliveryException(String message) {
        super(message, ErrorCode.EMAIL_DELIVERY_FAILED, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
