package com.sk.hotelreservationservice.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record NotificationDto(
        Long userId,
        String to,
        String subject,
        String type,
        String userFirstName,
        String userLastName,
        String managerFirstName,
        String managerLastName,
        String managerEmail,
        Instant reservationTime) {
}
