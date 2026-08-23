package com.sk.hotelnotificationservice.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record EmailDto(
        @Email String email) {
}
