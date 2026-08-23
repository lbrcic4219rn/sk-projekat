package com.sk.hoteluserservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record ManagerCreateDto(
        @NotBlank String username,
        @Length(min = 8, max = 20) String password,
        @Email String email,
        @Length(min = 9, max = 10) String phone,
        @DateTimeFormat LocalDate birthDate,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String hotelName,
        @DateTimeFormat LocalDate hireDate) {
}
