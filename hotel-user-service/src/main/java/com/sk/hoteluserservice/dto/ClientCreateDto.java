package com.sk.hoteluserservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record ClientCreateDto(
        @NotBlank String username,
        @Length(min = 8, max = 20) String password,
        @Email String email,
        @Length(min = 9, max = 10) String phone,
        @DateTimeFormat LocalDate birthDate,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String passportNumber,
        @NotNull Integer numberOfReservations) {
}
