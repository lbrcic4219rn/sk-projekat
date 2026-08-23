package com.sk.hoteluserservice.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users", indexes = {@Index(columnList = "username", unique = true), @Index(columnList = "email", unique = true)})
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String firstname;
    private String lastname;
    @ManyToOne(optional = false)
    private Role role;
    private String passportNumber;
    private Integer numberOfReservations;
    private String hotelName;
    private LocalDate hireDate;
    private boolean blocked = false;
}
