package com.sk.hoteluserservice.dto;

import lombok.Builder;

@Builder
public record TokenRequestDto(
        String username,
        String password) {
}
