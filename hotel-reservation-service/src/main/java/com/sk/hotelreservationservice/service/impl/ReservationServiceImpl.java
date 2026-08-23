package com.sk.hotelreservationservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.hotelreservationservice.client.userservice.UserServiceClient;
import com.sk.hotelreservationservice.domain.Hotel;
import com.sk.hotelreservationservice.domain.Reservation;
import com.sk.hotelreservationservice.domain.ReservationStatus;
import com.sk.hotelreservationservice.domain.RoomType;
import com.sk.hotelreservationservice.dto.NotificationDto;
import com.sk.hotelreservationservice.dto.ReservationCreateDto;
import com.sk.hotelreservationservice.dto.ReservationDto;
import com.sk.hotelreservationservice.dto.UserDto;
import com.sk.hotelreservationservice.exception.InvalidReservationException;
import com.sk.hotelreservationservice.exception.NotAvailableException;
import com.sk.hotelreservationservice.exception.NotFoundException;
import com.sk.hotelreservationservice.mapper.ReservationMapper;
import com.sk.hotelreservationservice.repository.ReservationRepository;
import com.sk.hotelreservationservice.repository.RoomTypeRepository;
import com.sk.hotelreservationservice.security.service.TokenService;
import com.sk.hotelreservationservice.service.HotelService;
import com.sk.hotelreservationservice.service.ReservationService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final String RESERVATION_NOT_FOUND = "Reservation with id: %d not found.";
    private static final String SUCCESSFUL_RESERVATION_EMAIL = "SUCCESSFUL_RESERVATION_EMAIL";
    private static final String CANCEL_RESERVATION_EMAIL = "CANCEL_RESERVATION_EMAIL";

    private final ReservationRepository reservationRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final ReservationMapper reservationMapper;
    private final HotelService hotelService;
    private final UserServiceClient userServiceClient;
    private final TokenService tokenService;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${destination.message}")
    private String notificationDestination;

    @Override
    public Page<ReservationDto> findAll(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(reservationMapper::reservationToReservationDto);
    }

    @Override
    public ReservationDto findById(Long id) {
        return reservationRepository.findById(id)
                .map(reservationMapper::reservationToReservationDto)
                .orElseThrow(() -> new NotFoundException(String.format(RESERVATION_NOT_FOUND, id)));
    }

    @Override
    public List<ReservationDto> findMyReservations(String authorization) {
        return reservationRepository.findAllByUserId(userId(authorization)).stream()
                .map(reservationMapper::reservationToReservationDto)
                .toList();
    }

    @Override
    public ReservationDto makeReservation(ReservationCreateDto dto, String authorization) {
        Long userId = userId(authorization);
        RoomType roomType = roomTypeRepository.findById(dto.roomTypeId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("RoomType with id: %d not found.", dto.roomTypeId())));

        Instant checkIn = dto.checkIn().truncatedTo(ChronoUnit.SECONDS);
        Instant checkOut = dto.checkOut().truncatedTo(ChronoUnit.SECONDS);
        validateDates(checkIn, checkOut);

        int free = hotelService.freeRooms(roomType, checkIn, checkOut);
        if (free <= 0) {
            throw new NotAvailableException(
                    String.format("No free rooms of type %s for the requested dates.", roomType.getName()));
        }

        Integer discount = userServiceClient.findDiscount(userId);
        Integer totalPrice = price(roomType, checkIn, checkOut, discount);

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setRoomType(roomType);
        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setDiscount(discount);
        reservation.setTotalPrice(totalPrice);
        reservation.setCreatedAt(Instant.now());
        reservationRepository.save(reservation);

        userServiceClient.incrementReservations(authorization);
        notify(reservation, SUCCESSFUL_RESERVATION_EMAIL, "successful reservation", authorization);
        log.info("Reservation {} created for user {} on roomType {}", reservation.getId(), userId, roomType.getId());

        return reservationMapper.reservationToReservationDto(reservation);
    }

    @Override
    public ReservationDto cancelReservation(Long id, String authorization) {
        Long userId = userId(authorization);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(RESERVATION_NOT_FOUND, id)));

        if (!reservation.getUserId().equals(userId)) {
            throw new NotFoundException(String.format(RESERVATION_NOT_FOUND, id));
        }
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new InvalidReservationException(String.format("Reservation %d is already cancelled.", id));
        }
        if (reservation.getCheckIn().isBefore(Instant.now())) {
            throw new InvalidReservationException("A reservation that already started cannot be cancelled.");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        userServiceClient.decrementReservations(authorization);
        notify(reservation, CANCEL_RESERVATION_EMAIL, "cancelled reservation", authorization);
        log.info("Reservation {} cancelled by user {}", id, userId);

        return reservationMapper.reservationToReservationDto(reservation);
    }

    private void validateDates(Instant checkIn, Instant checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            throw new InvalidReservationException("Check out must be after check in.");
        }
        if (checkIn.isBefore(Instant.now())) {
            throw new InvalidReservationException("Check in must be in the future.");
        }
    }

    private Integer price(RoomType roomType, Instant checkIn, Instant checkOut, Integer discount) {
        long nights = Math.max(1, Duration.between(checkIn, checkOut).toDays());
        int full = (int) (nights * roomType.getPricePerNight());
        return full - (full * discount / 100);
    }

    private void notify(Reservation reservation, String type, String subject, String authorization) {
        UserDto user = userServiceClient.findUser(reservation.getUserId(), authorization);
        Hotel hotel = reservation.getRoomType().getHotel();
        NotificationDto notification = NotificationDto.builder()
                .userId(reservation.getUserId())
                .to(user.email())
                .subject(subject)
                .type(type)
                .userFirstName(user.firstName())
                .userLastName(user.lastName())
                .managerFirstName(hotel.getManagerFirstName())
                .managerLastName(hotel.getManagerLastName())
                .managerEmail(hotel.getManagerEmail())
                .reservationTime(reservation.getCheckIn())
                .build();
        try {
            jmsTemplate.convertAndSend(notificationDestination, objectMapper.writeValueAsString(notification));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize {} notification for reservation {}", type, reservation.getId(), e);
        }
    }

    private Long userId(String authorization) {
        String[] auth = authorization.split(" ");
        Claims claims = tokenService.parseToken(auth[1])
                .orElseThrow(() -> new InvalidReservationException("Invalid token."));
        return Long.valueOf(claims.get("id", Integer.class));
    }
}
