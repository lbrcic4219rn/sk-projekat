package com.sk.hotelreservationservice.dto;

import lombok.Builder;

@Builder
public record RoomTypeDto(
        Long id,
        String name,
        String description,
        Integer pricePerNight,
        Integer totalRooms,
        Long hotelId,
        String hotelName) {
}
