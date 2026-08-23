package com.sk.hoteluserservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends CustomException {

    public InvalidCredentialsException(String message) {
        super(message, ErrorCode.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }
}
