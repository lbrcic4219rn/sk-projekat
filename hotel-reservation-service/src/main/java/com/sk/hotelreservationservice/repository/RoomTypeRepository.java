package com.sk.hotelreservationservice.repository;

import com.sk.hotelreservationservice.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    List<RoomType> findAllByHotelId(Long hotelId);
}
