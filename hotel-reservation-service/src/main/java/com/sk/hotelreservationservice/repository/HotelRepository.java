package com.sk.hotelreservationservice.repository;

import com.sk.hotelreservationservice.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findAllByCityIgnoreCase(String city);
}
