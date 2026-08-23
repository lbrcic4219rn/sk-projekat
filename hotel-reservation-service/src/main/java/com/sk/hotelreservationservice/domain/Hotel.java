package com.sk.hotelreservationservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String city;
    private String managerFirstName;
    private String managerLastName;
    private String managerEmail;

    public Hotel(String name, String description, String city, String managerFirstName, String managerLastName,
                 String managerEmail) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.managerFirstName = managerFirstName;
        this.managerLastName = managerLastName;
        this.managerEmail = managerEmail;
    }
}
