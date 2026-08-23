package com.sk.hotelreservationservice.controller;

import com.sk.hotelreservationservice.dto.AvailabilityDto;
import com.sk.hotelreservationservice.dto.HotelDto;
import com.sk.hotelreservationservice.dto.RoomTypeDto;
import com.sk.hotelreservationservice.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "Get all hotels")
    @GetMapping
    public ResponseEntity<Page<HotelDto>> getAllHotels(Pageable pageable) {
        return new ResponseEntity<>(hotelService.findAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get hotel by id")
    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(hotelService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Search hotels by city")
    @GetMapping("/search")
    public ResponseEntity<List<HotelDto>> searchHotels(@RequestParam("city") String city) {
        return new ResponseEntity<>(hotelService.findByCity(city), HttpStatus.OK);
    }

    @Operation(summary = "Get room types of a hotel")
    @GetMapping("/{id}/roomTypes")
    public ResponseEntity<List<RoomTypeDto>> getRoomTypes(@PathVariable("id") Long id) {
        return new ResponseEntity<>(hotelService.findRoomTypes(id), HttpStatus.OK);
    }

    @Operation(summary = "Check how many rooms of a type are free in a date range")
    @GetMapping("/roomType/{id}/availability")
    public ResponseEntity<AvailabilityDto> checkAvailability(
            @PathVariable("id") Long id,
            @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant checkIn,
            @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant checkOut) {
        return new ResponseEntity<>(hotelService.checkAvailability(id, checkIn, checkOut), HttpStatus.OK);
    }
}
