package com.sk.hotelnotificationservice.domain;

import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Instant reservationTime;
    private Boolean notified;
    private String userEmail;
    private String userFirstName;
    private String userLastName;

    public Reservation(Long userId, Instant reservationTime, Boolean notified, String userEmail, String userFirstName, String userLastName) {
        this.userId = userId;
        this.reservationTime = reservationTime;
        this.notified = notified;
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

}
