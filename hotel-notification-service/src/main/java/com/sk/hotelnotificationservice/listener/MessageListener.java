package com.sk.hotelnotificationservice.listener;

import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.hotelnotificationservice.dto.NotificationDto;
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

        log.info("Received notification message of type {} for user {}", dto.getType(), dto.getUserId());
        switch (dto.getType()) {
            case "ACTIVATION_EMAIL" -> notificationService.sendActivationEmail(dto);
            case "RESET_PASSWORD_EMAIL" -> notificationService.sendResetPasswordEmail(dto);
            case "SUCCESSFUL_RESERVATION_EMAIL" -> notificationService.sendSuccessfulReservationEmail(dto);
            case "CANCEL_RESERVATION_EMAIL" -> notificationService.sendCancelReservationEmail(dto);
            default -> {
                log.error("Unknown notification type: {}", dto.getType());
                throw new IllegalArgumentException("Unknown notification type: " + dto.getType());
            }
        }
    }
}
