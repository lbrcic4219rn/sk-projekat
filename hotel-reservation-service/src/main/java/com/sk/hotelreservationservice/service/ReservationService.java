package com.sk.hotelreservationservice.service;

import com.sk.hotelreservationservice.dto.ReservationCreateDto;
import com.sk.hotelreservationservice.dto.ReservationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {

    Page<ReservationDto> findAll(Pageable pageable);

    ReservationDto findById(Long id);

    List<ReservationDto> findMyReservations(String authorization);

    ReservationDto makeReservation(ReservationCreateDto reservationCreateDto, String authorization);

    ReservationDto cancelReservation(Long id, String authorization);
}
