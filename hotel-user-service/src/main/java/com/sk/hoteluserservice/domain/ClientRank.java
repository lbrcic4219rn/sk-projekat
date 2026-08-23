package com.sk.hoteluserservice.domain;

import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ClientRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Rank rank;
    private Integer discount;
    private Integer minNumberOfReservations;
    private Integer maxNumberOfReservations;

    public ClientRank(Integer minNumberOfReservations, Integer maxNumberOfReservations, Rank rank, Integer discount) {
        this.rank = rank;
        this.discount = discount;
        this.minNumberOfReservations = minNumberOfReservations;
        this.maxNumberOfReservations = maxNumberOfReservations;
    }

}
