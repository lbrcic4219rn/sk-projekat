package com.sk.hotelreservationservice.runner;

import com.sk.hotelreservationservice.domain.Hotel;
import com.sk.hotelreservationservice.domain.RoomType;
import com.sk.hotelreservationservice.repository.HotelRepository;
import com.sk.hotelreservationservice.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
@RequiredArgsConstructor
public class TestDataRunner implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    public void run(String... args) {
        Hotel belgrade = new Hotel("Hotel Moskva", "Hotel in the center of Belgrade", "Beograd",
                "Petar", "Petrovic", "manager@gmail.com");
        Hotel noviSad = new Hotel("Hotel Park", "Hotel near Petrovaradin", "Novi Sad",
                "Jovan", "Jovanovic", "manager2@gmail.com");
        hotelRepository.save(belgrade);
        hotelRepository.save(noviSad);

        roomTypeRepository.save(new RoomType("Single room", "Room with one bed", 3000, 5, belgrade));
        roomTypeRepository.save(new RoomType("Double room", "Room with two beds", 5000, 3, belgrade));
        roomTypeRepository.save(new RoomType("Apartment", "Apartment with a living room", 9000, 1, belgrade));
        roomTypeRepository.save(new RoomType("Single room", "Room with one bed", 2500, 4, noviSad));
        roomTypeRepository.save(new RoomType("Double room", "Room with two beds", 4000, 2, noviSad));
    }
}
