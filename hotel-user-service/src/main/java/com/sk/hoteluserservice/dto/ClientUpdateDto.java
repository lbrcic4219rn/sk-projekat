package com.sk.hoteluserservice.dto;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ClientUpdateDto {
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
    private String newPassportNumber;
    @NotNull
    private Integer newNumberOfReservations;

}
