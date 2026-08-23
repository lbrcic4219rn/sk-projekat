package com.sk.hotelnotificationservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDetails> handleCustomException(CustomException exception) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorCode(exception.getErrorCode())
                .errorMessage(exception.getMessage())
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorDetails, exception.getHttpStatus());
    }
}
