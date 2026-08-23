package com.sk.hotelreservationservice.dto;

import lombok.Builder;

@Builder
public record AvailabilityDto(
        Long roomTypeId,
        Integer availableRooms,
        Boolean available) {
}
