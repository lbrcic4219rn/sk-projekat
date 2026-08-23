package com.sk.hotelreservationservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer pricePerNight;
    private Integer totalRooms;
    @ManyToOne(optional = false)
    private Hotel hotel;

    public RoomType(String name, String description, Integer pricePerNight, Integer totalRooms, Hotel hotel) {
        this.name = name;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.totalRooms = totalRooms;
        this.hotel = hotel;
    }
}
