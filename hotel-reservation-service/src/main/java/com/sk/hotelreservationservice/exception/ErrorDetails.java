package com.sk.hotelreservationservice.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.Builder;

@Builder
public record ErrorDetails(
        @JsonProperty("error_code") ErrorCode errorCode,
        @JsonProperty("error_message") String errorMessage,
        Instant timestamp) {
}
