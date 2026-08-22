package com.sk.hoteluserservice.domain;

import javax.persistence.*;

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

    public ClientRank(){

    }

    public ClientRank(Integer minNumberOfReservations, Integer maxNumberOfReservations, Rank rank, Integer discount) {
        this.rank = rank;
        this.discount = discount;
        this.minNumberOfReservations = minNumberOfReservations;
        this.maxNumberOfReservations = maxNumberOfReservations;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getMinNumberOfReservations() {
        return minNumberOfReservations;
    }

    public void setMinNumberOfReservations(Integer minNumberOfReservations) {
        this.minNumberOfReservations = minNumberOfReservations;
    }

    public Integer getMaxNumberOfReservations() {
        return maxNumberOfReservations;
    }

    public void setMaxNumberOfReservations(Integer maxNumberOfReservations) {
        this.maxNumberOfReservations = maxNumberOfReservations;
    }
}
