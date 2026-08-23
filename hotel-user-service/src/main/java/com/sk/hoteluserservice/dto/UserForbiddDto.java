package com.sk.hoteluserservice.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class UserForbiddDto {

    @Email
    private String email;
    @NotBlank
    private String username;
    private boolean blocked;

}
