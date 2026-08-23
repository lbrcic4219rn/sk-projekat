package com.sk.hotelreservationservice.controller;

import com.sk.hotelreservationservice.dto.ReservationCreateDto;
import com.sk.hotelreservationservice.dto.ReservationDto;
import com.sk.hotelreservationservice.security.CheckSecurity;
import com.sk.hotelreservationservice.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "Get all reservations")
    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<Page<ReservationDto>> getAllReservations(
            @RequestHeader("Authorization") String authorization, Pageable pageable) {
        return new ResponseEntity<>(reservationService.findAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get reservations of the logged in client")
    @GetMapping("/myReservations")
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<List<ReservationDto>> getMyReservations(
            @RequestHeader("Authorization") String authorization) {
        return new ResponseEntity<>(reservationService.findMyReservations(authorization), HttpStatus.OK);
    }

    @Operation(summary = "Get reservation by id")
    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_CLIENT"})
    public ResponseEntity<ReservationDto> getReservationById(
            @RequestHeader("Authorization") String authorization, @PathVariable("id") Long id) {
        return new ResponseEntity<>(reservationService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Make a reservation")
    @PostMapping
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<ReservationDto> makeReservation(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ReservationCreateDto reservationCreateDto) {
        return new ResponseEntity<>(
                reservationService.makeReservation(reservationCreateDto, authorization), HttpStatus.CREATED);
    }

    @Operation(summary = "Cancel a reservation")
    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<ReservationDto> cancelReservation(
            @RequestHeader("Authorization") String authorization, @PathVariable("id") Long id) {
        return new ResponseEntity<>(reservationService.cancelReservation(id, authorization), HttpStatus.OK);
    }
}
