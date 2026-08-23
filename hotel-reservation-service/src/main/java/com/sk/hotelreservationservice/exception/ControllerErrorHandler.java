package com.sk.hotelreservationservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDetails> handleCustomException(CustomException exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getErrorCode(), exception.getMessage(), Instant.now());
        return new ResponseEntity<>(errorDetails, exception.getHttpStatus());
    }
}