package com.sk.hotelreservationservice.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ReservationDto(
        Long id,
        Long userId,
        Long roomTypeId,
        String roomTypeName,
        Long hotelId,
        String hotelName,
        Instant checkIn,
        Instant checkOut,
        String status,
        Integer discount,
        Integer totalPrice) {
}
