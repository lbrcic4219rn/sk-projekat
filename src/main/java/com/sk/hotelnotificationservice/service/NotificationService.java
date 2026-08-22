package com.sk.hotelnotificationservice.service;

import com.sk.hotelnotificationservice.domain.Notification;
import com.sk.hotelnotificationservice.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface NotificationService {
    List<Notification> findNotificationsInDateRange(Date endDate, Date startDate);
    List<Notification> findNotificationsByEmail(String email);
    List<Notification> findNotificationsByType(String notificationType);

    Page<Notification> findAll(Pageable pageable);
    void sendActivationEmail(NotificationDto dto);
    void sendResetPasswordEmail(NotificationDto dto);
    void sendSuccessfulReservationEmail(NotificationDto dto);
    void sendCancelReservationEmail(NotificationDto dto);
    void send2DaysReminderEmail();

    List<Notification> findUserNotifications(String authorization, Pageable pageable);
}
