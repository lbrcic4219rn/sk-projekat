package com.sk.hotelnotificationservice.controller;

import com.sk.hotelnotificationservice.domain.Notification;
import com.sk.hotelnotificationservice.dto.DateRangeDto;
import com.sk.hotelnotificationservice.dto.EmailDto;
import com.sk.hotelnotificationservice.dto.NotificationTypeDto;
import com.sk.hotelnotificationservice.security.CheckSecurity;
import com.sk.hotelnotificationservice.service.NotificationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @ApiOperation(value = "Get all notifications")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
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
        Page p = new PageImpl(notifications, pageable, notifications.size());
        return new ResponseEntity<>(p, HttpStatus.OK);
    }


    @PostMapping("/getNotificationsByType")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<Notification>> getNotificationsByType(@RequestHeader("Authorization") String authorization,
                                                                     @RequestBody @Valid NotificationTypeDto dto, Pageable pageable) {
        List<Notification> notifications = notificationService.findNotificationsByType(dto.getType());
        Page p = new PageImpl(notifications, pageable, notifications.size());
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping("/getNotificationsByEmail")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<Notification>> getNotificationsByEmail(@RequestHeader("Authorization") String authorization,
                                                                     @RequestBody @Valid EmailDto dto, Pageable pageable) {
        List<Notification> notifications = notificationService.findNotificationsByEmail(dto.getEmail());
        Page p = new PageImpl(notifications, pageable, notifications.size());
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping("/getNotificationsInDateRange")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<Notification>> getNotificationsInDateRange(@RequestHeader("Authorization") String authorization,
                                                                          @RequestBody @Valid DateRangeDto dto, Pageable pageable) {
        List<Notification> notifications = notificationService.findNotificationsInDateRange(dto.getStartDate(), dto.getEndDate());
        Page p = new PageImpl(notifications, pageable, notifications.size());
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

}
