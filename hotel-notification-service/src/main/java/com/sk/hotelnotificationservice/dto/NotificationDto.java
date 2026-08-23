package com.sk.hotelnotificationservice.dto;

import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {
    private Long userId;
    private String to;//email
    private String subject;
    private String type;//ACTIVATION_EMAIL, RESET_PASSWORD_EMAIL, SUCCESSFUL_RESERVATION_EMAIL, CANCEL_RESERVATION_EMAIL, TWO_DAYS_REMINDER_EMAIL
    private String userFirstName;
    private String userLastName;
    private String managerFirstName;
    private String managerLastName;
    private String managerEmail;
    private Instant reservationTime;

    public NotificationDto(Long userId, String to, String subject, String type, String userFirstName, String userLastName) {
        this.userId = userId;
        this.to = to;
        this.subject = subject;
        this.type = type;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

}