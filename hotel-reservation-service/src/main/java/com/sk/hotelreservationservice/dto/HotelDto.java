package com.sk.hotelreservationservice.dto;

import lombok.Builder;

@Builder
public record HotelDto(
        Long id,
        String name,
        String description,
        String city,
        String managerFirstName,
        String managerLastName,
        String managerEmail) {
}
