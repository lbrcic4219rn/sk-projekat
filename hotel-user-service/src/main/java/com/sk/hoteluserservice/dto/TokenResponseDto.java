package com.sk.hoteluserservice.dto;

import lombok.Builder;

@Builder
public record TokenResponseDto(
        String token) {
}
