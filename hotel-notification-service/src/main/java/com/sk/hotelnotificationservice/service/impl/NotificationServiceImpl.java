package com.sk.hotelnotificationservice.service.impl;

import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

import com.sk.hotelnotificationservice.domain.Notification;
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
    private static final String GREETING = "Pozdrav ";
    private static final String EMAIL_SENT_LOG = "Email sent to {}";

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
                .orElseThrow(() -> new IllegalArgumentException("Invalid token."));
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
        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        String content = GREETING + dto.getUserFirstName() + " " + dto.getUserLastName() + ", \n" + "Kliknite na link za aktiviranje naloga.";
        message.setText(content);
        mailSender.send(message);
        log.info(EMAIL_SENT_LOG, Arrays.toString(message.getTo()));

        Notification notification = new Notification(dto.getUserId(), dto.getTo(), dto.getSubject(), content, "ACTIVATION_EMAIL", Instant.now());
        notificationRepository.save(notification);
    }

    @Override
    public void sendResetPasswordEmail(NotificationDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        String content = GREETING + dto.getUserFirstName() + " " + dto.getUserLastName() + ", \n" + "Uspesno ste resetovali svoju lozinku.";
        message.setText(content);
        mailSender.send(message);
        log.info(EMAIL_SENT_LOG, Arrays.toString(message.getTo()));

        Notification notification = new Notification(dto.getUserId(), dto.getTo(), dto.getSubject(), content, "RESET_PASSWORD_EMAIL", Instant.now());
        notificationRepository.save(notification);
    }

    @Override
    public void sendSuccessfulReservationEmail(NotificationDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        String content = GREETING + dto.getUserFirstName() + " " + dto.getUserLastName() + ", \n" + "Uspesno ste rezervisali smestaj.";
        message.setText(content);
        mailSender.send(message);
        log.info(EMAIL_SENT_LOG, Arrays.toString(message.getTo()));

        Notification notification = new Notification(dto.getUserId(), dto.getTo(), dto.getSubject(), content, "SUCCESSFUL_RESERVATION_EMAIL", Instant.now());
        notificationRepository.save(notification);

        Reservation reservation = new Reservation(dto.getUserId(), dto.getReservationTime(),false, dto.getTo(), dto.getUserFirstName(), dto.getUserLastName());
        reservationRepository.save(reservation);

        SimpleMailMessage message2 = new SimpleMailMessage();
        message2.setFrom(FROM_ADDRESS);
        message2.setTo(dto.getManagerEmail());
        message2.setSubject(dto.getSubject());
        String content2 = GREETING + dto.getManagerFirstName() + " " + dto.getManagerLastName() + ", \n" +
                "Klijent " + dto.getUserFirstName() + " " + dto.getUserLastName() + " je uspesno rezervisao smestaj u vasem hotelu.";
        message2.setText(content2);
        mailSender.send(message2);
        log.info(EMAIL_SENT_LOG, Arrays.toString(message2.getTo()));

        Notification notification2 = new Notification(dto.getUserId(), dto.getManagerEmail(), dto.getSubject(), content2, "SUCCESSFUL_RESERVATION_EMAIL", Instant.now());
        notificationRepository.save(notification2);
    }

    @Override
    public void sendCancelReservationEmail(NotificationDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        String content = GREETING + dto.getUserFirstName() + " " + dto.getUserLastName() + ", \n" + "Uspesno ste otkazali rezervaciju.";
        message.setText(content);
        mailSender.send(message);
        log.info(EMAIL_SENT_LOG, Arrays.toString(message.getTo()));

        Notification notification = new Notification(dto.getUserId(), dto.getTo(), dto.getSubject(), content, "CANCEL_RESERVATION_EMAIL", Instant.now());
        notificationRepository.save(notification);

        Reservation reservation = reservationRepository.findReservationByUserEmailAndUserFirstNameAndUserLastName(dto.getTo(), dto.getUserFirstName(), dto.getManagerLastName());
        reservationRepository.delete(reservation);

        SimpleMailMessage message2 = new SimpleMailMessage();
        message2.setFrom(FROM_ADDRESS);
        message2.setTo(dto.getManagerEmail());
        message2.setSubject(dto.getSubject());
        String content2 = GREETING + dto.getManagerFirstName() + " " + dto.getManagerLastName() + ", \n" +
                "Klijent " + dto.getUserFirstName() + " " + dto.getUserLastName() + " je otkazao rezervaciju u vasem hotelu.";
        message2.setText(content2);
        mailSender.send(message2);
        log.info(EMAIL_SENT_LOG, Arrays.toString(message2.getTo()));

        Notification notification2 = new Notification(dto.getUserId(), dto.getManagerEmail(), dto.getSubject(), content2, "CANCEL_RESERVATION_EMAIL", Instant.now());
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
                            ",\n" + "Podsecamo Vas da je Vasa rezervacija za 2 dana.";
                    message.setText(content);
                    mailSender.send(message);
        log.info(EMAIL_SENT_LOG, Arrays.toString(message.getTo()));

                    Notification notification = new Notification(reservation.getUserId(), reservation.getUserEmail(), "2 days reminder", content, "TWO_DAYS_REMINDER_EMAIL", Instant.now());
                    notificationRepository.save(notification);

                    reservation.setNotified(true);
                    reservationRepository.save(reservation);
        });
    }

}
