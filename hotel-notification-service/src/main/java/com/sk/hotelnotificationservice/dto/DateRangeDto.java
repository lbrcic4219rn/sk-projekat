package com.sk.hotelnotificationservice.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DateRangeDto {
    @DateTimeFormat
    private Date startDate;
    @DateTimeFormat
    private Date endDate;

    public DateRangeDto(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public DateRangeDto(){

    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
