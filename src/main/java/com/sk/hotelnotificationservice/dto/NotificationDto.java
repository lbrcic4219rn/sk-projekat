package com.sk.hotelnotificationservice.dto;

import java.time.Instant;

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

    public NotificationDto(Long userId, String to, String subject, String type, String userFirstName, String userLastName,
                           String managerFirstName, String managerLastName, String managerEmail, Instant reservationTime) {
        this.userId = userId;
        this.to = to;
        this.subject = subject;
        this.type = type;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.managerFirstName = managerFirstName;
        this.managerLastName = managerLastName;
        this.managerEmail = managerEmail;
        this.reservationTime = reservationTime;
    }

    public NotificationDto(){

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getManagerFirstName() {
        return managerFirstName;
    }

    public void setManagerFirstName(String managerFirstName) {
        this.managerFirstName = managerFirstName;
    }

    public String getManagerLastName() {
        return managerLastName;
    }

    public void setManagerLastName(String managerLastName) {
        this.managerLastName = managerLastName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public Instant getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Instant reservationTime) {
        this.reservationTime = reservationTime;
    }
}