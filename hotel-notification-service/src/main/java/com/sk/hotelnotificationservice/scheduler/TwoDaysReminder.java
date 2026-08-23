package com.sk.hotelnotificationservice.scheduler;

import lombok.RequiredArgsConstructor;

import com.sk.hotelnotificationservice.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TwoDaysReminder {
    private final NotificationService notificationService;

    @Scheduled(fixedDelay = 3000, initialDelay = 5000)
    public void remindClients(){
        notificationService.send2DaysReminderEmail();
    }
}
