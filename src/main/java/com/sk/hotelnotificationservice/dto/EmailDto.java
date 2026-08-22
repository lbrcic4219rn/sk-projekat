package com.sk.hotelnotificationservice.dto;

import javax.validation.constraints.Email;

public class EmailDto {
    @Email
    private String email;

    public EmailDto(String email) {
        this.email = email;
    }
    public EmailDto(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
