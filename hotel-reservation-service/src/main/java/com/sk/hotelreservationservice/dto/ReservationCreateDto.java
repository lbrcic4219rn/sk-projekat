package com.sk.hotelreservationservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ReservationCreateDto(
        @NotNull Long roomTypeId,
        @NotNull Instant checkIn,
        @NotNull Instant checkOut) {
}
