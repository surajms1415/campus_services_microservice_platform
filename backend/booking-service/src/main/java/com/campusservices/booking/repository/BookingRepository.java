package com.campusservices.booking.repository;

import com.campusservices.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.facilityId = :facilityId " +
           "AND b.bookingDate = :date AND b.status IN ('PENDING', 'APPROVED') " +
           "AND ((b.startTime < :end AND b.endTime > :start))")
    long countOverlappingBookings(@Param("facilityId") Long facilityId, 
                                  @Param("date") LocalDate date, 
                                  @Param("start") LocalTime start, 
                                  @Param("end") LocalTime end);
}
