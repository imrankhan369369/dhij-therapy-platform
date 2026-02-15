package com.dhij.app.com.dhij.app.repository;



import com.dhij.app.com.dhij.app.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByHelperIdAndStartTimeBetween(Long helperId, LocalDateTime dayStart, LocalDateTime dayEnd);
    Optional<Booking> findByHelperIdAndStartTime(Long helperId, LocalDateTime startTime);
}