package com.sk.hotelreservationservice.mapper;

import com.sk.hotelreservationservice.domain.Hotel;
import com.sk.hotelreservationservice.domain.Reservation;
import com.sk.hotelreservationservice.domain.RoomType;
import com.sk.hotelreservationservice.dto.HotelDto;
import com.sk.hotelreservationservice.dto.ReservationDto;
import com.sk.hotelreservationservice.dto.RoomTypeDto;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public HotelDto hotelToHotelDto(Hotel hotel) {
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .city(hotel.getCity())
                .managerFirstName(hotel.getManagerFirstName())
                .managerLastName(hotel.getManagerLastName())
                .managerEmail(hotel.getManagerEmail())
                .build();
    }

    public RoomTypeDto roomTypeToRoomTypeDto(RoomType roomType) {
        return RoomTypeDto.builder()
                .id(roomType.getId())
                .name(roomType.getName())
                .description(roomType.getDescription())
                .pricePerNight(roomType.getPricePerNight())
                .totalRooms(roomType.getTotalRooms())
                .hotelId(roomType.getHotel().getId())
                .hotelName(roomType.getHotel().getName())
                .build();
    }

    public ReservationDto reservationToReservationDto(Reservation reservation) {
        RoomType roomType = reservation.getRoomType();
        return ReservationDto.builder()
                .id(reservation.getId())
                .userId(reservation.getUserId())
                .roomTypeId(roomType.getId())
                .roomTypeName(roomType.getName())
                .hotelId(roomType.getHotel().getId())
                .hotelName(roomType.getHotel().getName())
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .status(reservation.getStatus().name())
                .discount(reservation.getDiscount())
                .totalPrice(reservation.getTotalPrice())
                .build();
    }
}
