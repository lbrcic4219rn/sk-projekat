package com.sk.hotelnotificationservice.dto;

import javax.validation.constraints.NotBlank;

public class NotificationTypeDto {
    @NotBlank
    private String type;

    public NotificationTypeDto(String type) {
        this.type = type;
    }
    public NotificationTypeDto(){

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
