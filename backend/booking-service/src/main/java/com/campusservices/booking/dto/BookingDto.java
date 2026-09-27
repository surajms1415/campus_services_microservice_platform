package com.campusservices.booking.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import com.campusservices.booking.entity.Booking.Status;

@Data
public class BookingDto {
    private Long id;
    private Long userId;
    private Long facilityId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Status status;
    private LocalDateTime createdAt;
}
