package com.sk.hotelnotificationservice.service.impl;

import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

import com.sk.hotelnotificationservice.domain.Notification;
import com.sk.hotelnotificationservice.domain.NotificationType;
import com.sk.hotelnotificationservice.exception.InvalidTokenException;
import com.sk.hotelnotificationservice.domain.Reservation;
import com.sk.hotelnotificationservice.dto.NotificationDto;
import com.sk.hotelnotificationservice.repository.NotificationRepository;
import com.sk.hotelnotificationservice.repository.ReservationRepository;
import com.sk.hotelnotificationservice.security.service.TokenService;
import com.sk.hotelnotificationservice.service.NotificationService;
import io.jsonwebtoken.Claims;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService{

    private static final String FROM_ADDRESS = "hotelnotificationservice@gmail.com";
    private static final String GREETING = "Hello ";
    private static final String EMAIL_SENT_LOG = "{} sent to {} ({})";

    private final NotificationRepository notificationRepository;
    private final ReservationRepository reservationRepository;
    private final JavaMailSender mailSender;
    private final TokenService tokenService;

    @Override
    public List<Notification> findNotificationsInDateRange(Instant startDate, Instant endDate) {
        return this.notificationRepository.findAllByDateCreatedBetween(startDate, endDate);
    }

    @Override
    public List<Notification> findNotificationsByEmail(String email) {
        return this.notificationRepository.findAllByTo(email);
    }

    @Override
    public List<Notification> findNotificationsByType(String type) {
        return notificationRepository.findAllByType(type);
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    public List<Notification> findUserNotifications(String authorization, Pageable pageable) {
        String[] auth = authorization.split(" ");
        String token = auth[1];
        Claims claims = tokenService.parseToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token."));
        Integer intId = claims.get("id", Integer.class);
        Long userId = Long.valueOf(intId);

        Page<Notification> page = notificationRepository.findAll(pageable);
        List<Notification> notifs = page.getContent();
        List<Notification> resultList = new ArrayList<>();
        for (Notification n: notifs) {
            if(Objects.equals(n.getUserId(), userId)){
                resultList.add(n);
            }
        }
        return resultList;
    }

    @Override
    public void sendActivationEmail(NotificationDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(dto.to());
        message.setSubject(dto.subject());
        String content = GREETING + dto.userFirstName() + " " + dto.userLastName() + ", \n" + "Click the link to activate your account.";
        message.setText(content);
        mailSender.send(message);
        log.info(EMAIL_SENT_LOG, NotificationType.ACTIVATION, Arrays.toString(message.getTo()), "client");

        Notification notification = new Notification(dto.userId(), dto.to(), dto.subject(), content, NotificationType.ACTIVATION, Instant.now());
        notificationRepository.save(notification);
    }

    @Override
    public void sendResetPasswordEmail(NotificationDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(dto.to());
        message.setSubject(dto.subject());
        String content = GREETING + dto.userFirstName() + " " + dto.userLastName() + ", \n" + "You have successfully reset your password.";
        message.setText(content);
        mailSender.send(message);
        log.info(EMAIL_SENT_LOG, NotificationType.RESET_PASSWORD, Arrays.toString(message.getTo()), "client");

        Notification notification = new Notification(dto.userId(), dto.to(), dto.subject(), content, NotificationType.RESET_PASSWORD, Instant.now());
        notificationRepository.save(notification);
    }

    @Override
    public void sendSuccessfulReservationEmail(NotificationDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(dto.to());
        message.setSubject(dto.subject());
        String content = GREETING + dto.userFirstName() + " " + dto.userLastName() + ", \n" + "You have successfully booked your accommodation.";
        message.setText(content);
        mailSender.send(message);
        log.info(EMAIL_SENT_LOG, NotificationType.SUCCESSFUL_RESERVATION, Arrays.toString(message.getTo()), "client");

        Notification notification = new Notification(dto.userId(), dto.to(), dto.subject(), content, NotificationType.SUCCESSFUL_RESERVATION, Instant.now());
        notificationRepository.save(notification);

        Reservation reservation = new Reservation(dto.userId(), dto.reservationTime(),false, dto.to(), dto.userFirstName(), dto.userLastName());
        reservationRepository.save(reservation);

        SimpleMailMessage message2 = new SimpleMailMessage();
        message2.setFrom(FROM_ADDRESS);
        message2.setTo(dto.managerEmail());
        message2.setSubject(dto.subject());
        String content2 = GREETING + dto.managerFirstName() + " " + dto.managerLastName() + ", \n" +
                "Client " + dto.userFirstName() + " " + dto.userLastName() + " has booked accommodation at your hotel.";
        message2.setText(content2);
        mailSender.send(message2);
        log.info(EMAIL_SENT_LOG, NotificationType.SUCCESSFUL_RESERVATION, Arrays.toString(message2.getTo()), "manager");

        Notification notification2 = new Notification(dto.userId(), dto.managerEmail(), dto.subject(), content2, NotificationType.SUCCESSFUL_RESERVATION, Instant.now());
        notificationRepository.save(notification2);
    }

    @Override
    public void sendCancelReservationEmail(NotificationDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(dto.to());
        message.setSubject(dto.subject());
        String content = GREETING + dto.userFirstName() + " " + dto.userLastName() + ", \n" + "You have successfully cancelled your reservation.";
        message.setText(content);
        mailSender.send(message);
        log.info(EMAIL_SENT_LOG, NotificationType.CANCEL_RESERVATION, Arrays.toString(message.getTo()), "client");

        Notification notification = new Notification(dto.userId(), dto.to(), dto.subject(), content, NotificationType.CANCEL_RESERVATION, Instant.now());
        notificationRepository.save(notification);

        Reservation reservation = reservationRepository.findReservationByUserEmailAndUserFirstNameAndUserLastName(dto.to(), dto.userFirstName(), dto.managerLastName());
        reservationRepository.delete(reservation);

        SimpleMailMessage message2 = new SimpleMailMessage();
        message2.setFrom(FROM_ADDRESS);
        message2.setTo(dto.managerEmail());
        message2.setSubject(dto.subject());
        String content2 = GREETING + dto.managerFirstName() + " " + dto.managerLastName() + ", \n" +
                "Client " + dto.userFirstName() + " " + dto.userLastName() + " has cancelled a reservation at your hotel.";
        message2.setText(content2);
        mailSender.send(message2);
        log.info(EMAIL_SENT_LOG, NotificationType.CANCEL_RESERVATION, Arrays.toString(message2.getTo()), "manager");

        Notification notification2 = new Notification(dto.userId(), dto.managerEmail(), dto.subject(), content2, NotificationType.CANCEL_RESERVATION, Instant.now());
        notificationRepository.save(notification2);
    }

    @Override
    public void send2DaysReminderEmail() {
        reservationRepository.findAllByNotifiedAndReservationTimeGreaterThan(false,
                Instant.now().minus(2, ChronoUnit.DAYS)).forEach( reservation ->{
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(FROM_ADDRESS);
                    message.setTo(reservation.getUserEmail());
                    message.setSubject("2 days reminder");
                    String content = GREETING + reservation.getUserFirstName() + " " + reservation.getUserLastName() +
                            ",\n" + "This is a reminder that your reservation starts in 2 days.";
                    message.setText(content);
                    mailSender.send(message);
                    log.info(EMAIL_SENT_LOG, NotificationType.TWO_DAYS_REMINDER, Arrays.toString(message.getTo()), "client");
                    Notification notification = new Notification(reservation.getUserId(), reservation.getUserEmail(), "2 days reminder", content, NotificationType.TWO_DAYS_REMINDER, Instant.now());
                    notificationRepository.save(notification);

                    reservation.setNotified(true);
                    reservationRepository.save(reservation);
        });
    }

}
