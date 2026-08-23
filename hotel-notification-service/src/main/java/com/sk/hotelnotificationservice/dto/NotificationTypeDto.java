package com.sk.hotelnotificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NotificationTypeDto(
        @NotBlank String type) {
}
