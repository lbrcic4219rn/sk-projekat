package com.sk.hotelnotificationservice.dto;

import java.time.Instant;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record DateRangeDto(
        @DateTimeFormat Instant startDate,
        @DateTimeFormat Instant endDate) {
}
