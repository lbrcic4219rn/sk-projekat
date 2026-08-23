package com.sk.hotelreservationservice.service;

import com.sk.hotelreservationservice.domain.RoomType;
import com.sk.hotelreservationservice.dto.AvailabilityDto;
import com.sk.hotelreservationservice.dto.HotelDto;
import com.sk.hotelreservationservice.dto.RoomTypeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface HotelService {

    Page<HotelDto> findAll(Pageable pageable);

    HotelDto findById(Long id);

    List<HotelDto> findByCity(String city);

    List<RoomTypeDto> findRoomTypes(Long hotelId);

    AvailabilityDto checkAvailability(Long roomTypeId, Instant checkIn, Instant checkOut);

    int freeRooms(RoomType roomType, Instant checkIn, Instant checkOut);
}
