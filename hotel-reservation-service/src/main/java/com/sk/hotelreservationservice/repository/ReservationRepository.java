package com.sk.hotelreservationservice.repository;

import com.sk.hotelreservationservice.domain.Reservation;
import com.sk.hotelreservationservice.domain.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByUserId(Long userId);

    Page<Reservation> findAllByUserId(Long userId, Pageable pageable);

    @Query("select count(r) from Reservation r where r.roomType.id = :roomTypeId and r.status = :status "
            + "and r.checkIn < :checkOut and :checkIn < r.checkOut")
    long countOverlapping(@Param("roomTypeId") Long roomTypeId,
                          @Param("status") ReservationStatus status,
                          @Param("checkIn") Instant checkIn,
                          @Param("checkOut") Instant checkOut);
}
