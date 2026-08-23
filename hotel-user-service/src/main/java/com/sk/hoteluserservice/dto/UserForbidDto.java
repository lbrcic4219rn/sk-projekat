package com.sk.hoteluserservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserForbidDto(
        @Email String email,
        @NotBlank String username,
        boolean blocked) {
}
