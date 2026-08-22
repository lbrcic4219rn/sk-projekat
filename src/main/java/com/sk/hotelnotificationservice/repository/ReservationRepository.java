package com.sk.hotelnotificationservice.repository;

import com.sk.hotelnotificationservice.domain.Notification;
import com.sk.hotelnotificationservice.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByNotifiedAndReservationTimeGreaterThan(Boolean notified, Instant reservationTime);
    Reservation findReservationByUserEmailAndUserFirstNameAndUserLastName(String userEmail, String userFirstName, String userLastName);
}
