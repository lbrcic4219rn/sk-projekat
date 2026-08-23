package com.sk.hoteluserservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record ManagerUpdateDto(
        @NotBlank String oldUsername,
        @Length(min = 8, max = 20) String oldPassword,
        @NotBlank String newUsername,
        @Length(min = 8, max = 20) String newPassword,
        @Email String newEmail,
        @Length(min = 9, max = 10) String newPhone,
        @NotBlank String newFirstName,
        @NotBlank String newLastName,
        @NotBlank String newHotelName,
        @DateTimeFormat LocalDate newHireDate) {
}
