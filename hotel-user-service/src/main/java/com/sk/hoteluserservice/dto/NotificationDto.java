package com.sk.hoteluserservice.dto;

import java.time.Instant;
import lombok.Builder;

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
