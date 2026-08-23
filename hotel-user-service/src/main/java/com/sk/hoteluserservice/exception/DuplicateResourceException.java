package com.sk.hoteluserservice.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends CustomException {

    public DuplicateResourceException(String message) {
        super(message, ErrorCode.DUPLICATE_RESOURCE, HttpStatus.CONFLICT);
    }
}
