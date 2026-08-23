package com.sk.hotelnotificationservice.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailDto {
    @Email
    private String email;

}
