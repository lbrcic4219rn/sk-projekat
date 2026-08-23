package com.sk.hotelnotificationservice.listener;

import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.hotelnotificationservice.domain.NotificationType;
import com.sk.hotelnotificationservice.dto.NotificationDto;
import com.sk.hotelnotificationservice.exception.UnknownNotificationTypeException;
import com.sk.hotelnotificationservice.service.NotificationService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @JmsListener(destination = "${destination.message}", concurrency = "5-10")
    public void notificationMessage(Message message) throws JMSException, JsonProcessingException {
        String json = ((TextMessage)message).getText();
        NotificationDto dto = objectMapper.readValue(json, NotificationDto.class);

        log.info("Received notification message of type {} for user {}", dto.type(), dto.userId());
        switch (dto.type()) {
            case NotificationType.ACTIVATION -> notificationService.sendActivationEmail(dto);
            case NotificationType.RESET_PASSWORD -> notificationService.sendResetPasswordEmail(dto);
            case NotificationType.SUCCESSFUL_RESERVATION -> notificationService.sendSuccessfulReservationEmail(dto);
            case NotificationType.CANCEL_RESERVATION -> notificationService.sendCancelReservationEmail(dto);
            default -> {
                log.error("Unknown notification type: {}", dto.type());
                throw new UnknownNotificationTypeException("Unknown notification type: " + dto.type());
            }
        }
    }
}
