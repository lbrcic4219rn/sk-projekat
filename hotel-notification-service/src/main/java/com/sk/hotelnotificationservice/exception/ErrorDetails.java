package com.sk.hotelnotificationservice.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorDetails(
        @JsonProperty("error_code") ErrorCode errorCode,
        @JsonProperty("error_message") String errorMessage,
        Instant timestamp) {
}
