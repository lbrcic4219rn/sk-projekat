package com.sk.hotelnotificationservice.domain;

import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;//id of user who receives this notification
    @Column(name = "recipient")
    private String to;
    private String subject;
    private String content;
    private String type; //ACTIVATION_EMAIL, RESET_PASSWORD_EMAIL, SUCCESSFUL_RESERVATION_EMAIL, CANCEL_RESERVATION_EMAIL, TWO_DAYS_REMINDER_EMAIL
    private Instant dateCreated;

    public Notification(Long userId, String to, String subject, String content, String type, Instant dateCreated) {
        this.userId = userId;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.type = type;
        this.dateCreated = dateCreated;
    }

}
