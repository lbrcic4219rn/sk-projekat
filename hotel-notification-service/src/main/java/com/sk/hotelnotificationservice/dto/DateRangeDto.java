package com.sk.hotelnotificationservice.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DateRangeDto {
    @DateTimeFormat
    private Instant startDate;
    @DateTimeFormat
    private Instant endDate;

}
