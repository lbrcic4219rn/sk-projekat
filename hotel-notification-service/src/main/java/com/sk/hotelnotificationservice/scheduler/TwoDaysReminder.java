package com.sk.hotelnotificationservice.scheduler;

import com.sk.hotelnotificationservice.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TwoDaysReminder {
    private NotificationService notificationService;

    public TwoDaysReminder(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelay = 3000, initialDelay = 5000)
    public void remindClients(){
        notificationService.send2DaysReminderEmail();
    }
}
