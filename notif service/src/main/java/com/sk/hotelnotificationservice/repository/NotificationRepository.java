package com.sk.hotelnotificationservice.repository;

import com.sk.hotelnotificationservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Override
    Optional<Notification> findById(Long id);
    List<Notification> findAllByType(String type);
    List<Notification> findAllByTo(String email);
    List<Notification> findAllByDateCreatedBetween(Date endDate, Date startDate);
}
