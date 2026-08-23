package com.sk.hotelnotificationservice.domain;

public final class NotificationType {

    public static final String ACTIVATION = "ACTIVATION_EMAIL";
    public static final String RESET_PASSWORD = "RESET_PASSWORD_EMAIL";
    public static final String SUCCESSFUL_RESERVATION = "SUCCESSFUL_RESERVATION_EMAIL";
    public static final String CANCEL_RESERVATION = "CANCEL_RESERVATION_EMAIL";
    public static final String TWO_DAYS_REMINDER = "TWO_DAYS_REMINDER_EMAIL";

    private NotificationType() {
    }
}
