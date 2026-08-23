package com.sk.hotelnotificationservice.scheduler;

import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

import com.sk.hotelnotificationservice.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TwoDaysReminder {
    private final NotificationService notificationService;

    @Scheduled(fixedDelay = 3000, initialDelay = 5000)
    public void remindClients(){
        log.info("Running two day reservation reminder job");
        notificationService.send2DaysReminderEmail();
    }
}
