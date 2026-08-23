package com.sk.hotelreservationservice.service.impl;

import com.sk.hotelreservationservice.domain.ReservationStatus;
import com.sk.hotelreservationservice.domain.RoomType;
import com.sk.hotelreservationservice.dto.AvailabilityDto;
import com.sk.hotelreservationservice.dto.HotelDto;
import com.sk.hotelreservationservice.dto.RoomTypeDto;
import com.sk.hotelreservationservice.exception.NotFoundException;
import com.sk.hotelreservationservice.mapper.ReservationMapper;
import com.sk.hotelreservationservice.repository.HotelRepository;
import com.sk.hotelreservationservice.repository.ReservationRepository;
import com.sk.hotelreservationservice.repository.RoomTypeRepository;
import com.sk.hotelreservationservice.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private static final String HOTEL_NOT_FOUND = "Hotel with id: %d not found.";
    private static final String ROOMTYPE_NOT_FOUND = "RoomType with id: %d not found.";

    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public Page<HotelDto> findAll(Pageable pageable) {
        return hotelRepository.findAll(pageable).map(reservationMapper::hotelToHotelDto);
    }

    @Override
    public HotelDto findById(Long id) {
        return hotelRepository.findById(id)
                .map(reservationMapper::hotelToHotelDto)
                .orElseThrow(() -> new NotFoundException(String.format(HOTEL_NOT_FOUND, id)));
    }

    @Override
    public List<HotelDto> findByCity(String city) {
        return hotelRepository.findAllByCityIgnoreCase(city).stream()
                .map(reservationMapper::hotelToHotelDto)
                .toList();
    }

    @Override
    public List<RoomTypeDto> findRoomTypes(Long hotelId) {
        if (!hotelRepository.existsById(hotelId)) {
            throw new NotFoundException(String.format(HOTEL_NOT_FOUND, hotelId));
        }
        return roomTypeRepository.findAllByHotelId(hotelId).stream()
                .map(reservationMapper::roomTypeToRoomTypeDto)
                .toList();
    }

    @Override
    public AvailabilityDto checkAvailability(Long roomTypeId, Instant checkIn, Instant checkOut) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new NotFoundException(String.format(ROOMTYPE_NOT_FOUND, roomTypeId)));
        int free = freeRooms(roomType, checkIn, checkOut);
        return AvailabilityDto.builder()
                .roomTypeId(roomTypeId)
                .availableRooms(free)
                .available(free > 0)
                .build();
    }

    @Override
    public int freeRooms(RoomType roomType, Instant checkIn, Instant checkOut) {
        long taken = reservationRepository.countOverlapping(roomType.getId(), ReservationStatus.CONFIRMED,
                checkIn, checkOut);
        return (int) (roomType.getTotalRooms() - taken);
    }
}
