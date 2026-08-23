package com.sk.hoteluserservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate birthDate;
    private String phone;
    //for clients
    private String passportNumber;
    private Integer numberOfReservations;
    //for managers
    private String hotelName;
    private LocalDate hireDate;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", birthDate=" + birthDate +
                ", phone='" + phone + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", numberOfReservations=" + numberOfReservations +
                ", hotelName='" + hotelName + '\'' +
                ", hireDate=" + hireDate +
                '}';
    }
}
