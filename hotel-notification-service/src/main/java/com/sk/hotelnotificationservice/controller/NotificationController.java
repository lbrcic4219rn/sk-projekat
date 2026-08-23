package com.sk.hotelnotificationservice.controller;

import lombok.RequiredArgsConstructor;

import com.sk.hotelnotificationservice.domain.Notification;
import com.sk.hotelnotificationservice.dto.DateRangeDto;
import com.sk.hotelnotificationservice.dto.EmailDto;
import com.sk.hotelnotificationservice.dto.NotificationTypeDto;
import com.sk.hotelnotificationservice.security.CheckSecurity;
import com.sk.hotelnotificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get all notifications")
    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<Notification>> getAllNotifications(@RequestHeader("Authorization") String authorization,
                                                          Pageable pageable) {

        return new ResponseEntity<>(notificationService.findAll(pageable), HttpStatus.OK);
    }
    @GetMapping("/getUserNotifications")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_CLIENT"})
    public ResponseEntity<Page<Notification>> getUserNotifications(@RequestHeader("Authorization") String authorization,
                                                                  Pageable pageable) {

        List<Notification> notifications = notificationService.findUserNotifications(authorization, pageable);
        Page<Notification> p = new PageImpl<>(notifications, pageable, notifications.size());
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping("/getNotificationsByType")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<Notification>> getNotificationsByType(@RequestHeader("Authorization") String authorization,
                                                                     @RequestBody @Valid NotificationTypeDto dto, Pageable pageable) {
        List<Notification> notifications = notificationService.findNotificationsByType(dto.getType());
        Page<Notification> p = new PageImpl<>(notifications, pageable, notifications.size());
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping("/getNotificationsByEmail")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<Notification>> getNotificationsByEmail(@RequestHeader("Authorization") String authorization,
                                                                     @RequestBody @Valid EmailDto dto, Pageable pageable) {
        List<Notification> notifications = notificationService.findNotificationsByEmail(dto.getEmail());
        Page<Notification> p = new PageImpl<>(notifications, pageable, notifications.size());
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping("/getNotificationsInDateRange")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<Notification>> getNotificationsInDateRange(@RequestHeader("Authorization") String authorization,
                                                                          @RequestBody @Valid DateRangeDto dto, Pageable pageable) {
        List<Notification> notifications = notificationService.findNotificationsInDateRange(dto.getStartDate(), dto.getEndDate());
        Page<Notification> p = new PageImpl<>(notifications, pageable, notifications.size());
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

}
