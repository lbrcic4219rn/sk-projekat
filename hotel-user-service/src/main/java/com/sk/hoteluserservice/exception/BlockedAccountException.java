package com.sk.hoteluserservice.exception;

import org.springframework.http.HttpStatus;

public class BlockedAccountException extends CustomException {

    public BlockedAccountException(String message) {
        super(message, ErrorCode.ACCOUNT_BLOCKED, HttpStatus.FORBIDDEN);
    }
}
