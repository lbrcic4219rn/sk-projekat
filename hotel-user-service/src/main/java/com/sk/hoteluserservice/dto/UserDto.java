package com.sk.hoteluserservice.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String username,
        LocalDate birthDate,
        String phone,
        String passportNumber,
        Integer numberOfReservations,
        String hotelName,
        LocalDate hireDate) {
}
