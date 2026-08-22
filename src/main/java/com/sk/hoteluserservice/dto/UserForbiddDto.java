package com.sk.hoteluserservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserForbiddDto {

    @Email
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private boolean blocked;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
