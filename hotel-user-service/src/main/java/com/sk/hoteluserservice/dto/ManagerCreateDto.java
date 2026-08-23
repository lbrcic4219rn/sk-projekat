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
public class ManagerCreateDto {
    @NotBlank
    private String username;
    @Length(min = 8, max = 20)
    private String password;
    @Email
    private String email;
    @Length(min = 9, max = 10)
    private String phone;
    @DateTimeFormat
    private LocalDate birthDate;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String hotelName;
    @DateTimeFormat
    private LocalDate hireDate;

}
