package com.sk.hoteluserservice.dto;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class ManagerUpdateDto {
    @NotBlank
    private String oldUsername;
    @Length(min = 8, max = 20)
    private String oldPassword;
    @NotBlank
    private String newUsername;
    @Length(min = 8, max = 20)
    private String newPassword;
    @Email
    private String newEmail;
    @Length(min = 9, max = 10)
    private String newPhone;
    @NotBlank
    private String newFirstName;
    @NotBlank
    private String newLastName;
    @NotBlank
    private String newHotelName;
    @DateTimeFormat
    private LocalDate newHireDate;

}
