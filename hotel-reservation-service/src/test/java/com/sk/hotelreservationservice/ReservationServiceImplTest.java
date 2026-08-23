package com.sk.hotelreservationservice;

import com.sk.hotelreservationservice.client.userservice.UserServiceClient;
import com.sk.hotelreservationservice.dto.AvailabilityDto;
import com.sk.hotelreservationservice.dto.ReservationCreateDto;
import com.sk.hotelreservationservice.dto.ReservationDto;
import com.sk.hotelreservationservice.dto.UserDto;
import com.sk.hotelreservationservice.exception.InvalidReservationException;
import com.sk.hotelreservationservice.exception.NotAvailableException;
import com.sk.hotelreservationservice.security.service.TokenService;
import com.sk.hotelreservationservice.service.HotelService;
import com.sk.hotelreservationservice.service.ReservationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.jms.core.JmsTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class ReservationServiceImplTest {

    private static final Long APARTMENT_ID = 3L;

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private TokenService tokenService;

    @MockitoBean
    private UserServiceClient userServiceClient;
    @MockitoBean
    private JmsTemplate jmsTemplate;

    @Test
    void reservationTakesTheLastRoomAndAppliesTheDiscount() {
        given(userServiceClient.findDiscount(anyLong())).willReturn(10);
        given(userServiceClient.findUser(anyLong(), anyString())).willReturn(user());

        Instant checkIn = Instant.now().plus(10, ChronoUnit.DAYS);
        Instant checkOut = checkIn.plus(4, ChronoUnit.DAYS);

        ReservationDto reservation = reservationService.makeReservation(createDto(checkIn, checkOut), token(1L));

        assertEquals(32400, reservation.totalPrice());
        assertEquals("CONFIRMED", reservation.status());

        AvailabilityDto availability = hotelService.checkAvailability(APARTMENT_ID, checkIn, checkOut);
        assertEquals(0, availability.availableRooms());

        assertThrows(NotAvailableException.class,
                () -> reservationService.makeReservation(createDto(checkIn, checkOut), token(2L)));

        assertEquals(1, hotelService.checkAvailability(APARTMENT_ID, checkOut,
                checkOut.plus(2, ChronoUnit.DAYS)).availableRooms());

        reservationService.cancelReservation(reservation.id(), token(1L));
        assertEquals(1, hotelService.checkAvailability(APARTMENT_ID, checkIn, checkOut).availableRooms());
    }

    @Test
    void checkOutBeforeCheckInIsRejected() {
        Instant checkIn = Instant.now().plus(10, ChronoUnit.DAYS);
        assertThrows(InvalidReservationException.class, () -> reservationService
                .makeReservation(createDto(checkIn, checkIn.minus(1, ChronoUnit.DAYS)), token(1L)));
    }

    @Test
    void reservationInThePastIsRejected() {
        Instant checkIn = Instant.now().minus(2, ChronoUnit.DAYS);
        assertThrows(InvalidReservationException.class, () -> reservationService
                .makeReservation(createDto(checkIn, checkIn.plus(3, ChronoUnit.DAYS)), token(1L)));
    }

    @Test
    void anotherClientCannotCancelSomeoneElsesReservation() {
        given(userServiceClient.findDiscount(anyLong())).willReturn(0);
        given(userServiceClient.findUser(anyLong(), anyString())).willReturn(user());

        Instant checkIn = Instant.now().plus(30, ChronoUnit.DAYS);
        ReservationDto reservation = reservationService
                .makeReservation(createDto(checkIn, checkIn.plus(1, ChronoUnit.DAYS)), token(1L));

        assertThrows(com.sk.hotelreservationservice.exception.NotFoundException.class,
                () -> reservationService.cancelReservation(reservation.id(), token(99L)));
    }

    private ReservationCreateDto createDto(Instant checkIn, Instant checkOut) {
        return ReservationCreateDto.builder()
                .roomTypeId(APARTMENT_ID)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .build();
    }

    private String token(Long userId) {
        Claims claims = Jwts.claims()
                .add("id", userId.intValue())
                .add("role", "ROLE_CLIENT")
                .build();
        return "Bearer " + tokenService.generate(claims);
    }

    private UserDto user() {
        return UserDto.builder()
                .id(1L)
                .email("client@gmail.com")
                .firstName("Marko")
                .lastName("Markovic")
                .username("marko")
                .build();
    }
}
