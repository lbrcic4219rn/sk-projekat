package com.sk.hotelreservationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String username) {
}
