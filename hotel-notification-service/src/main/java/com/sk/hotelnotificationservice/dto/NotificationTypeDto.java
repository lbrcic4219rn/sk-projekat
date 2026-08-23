package com.sk.hotelnotificationservice.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationTypeDto {
    @NotBlank
    private String type;

}
