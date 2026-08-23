package com.sk.hoteluserservice.exception;

import org.springframework.http.HttpStatus;

public class ConfigurationException extends CustomException {

    public ConfigurationException(String message) {
        super(message, ErrorCode.CONFIGURATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
