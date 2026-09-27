package com.campusservices.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequest {
    @NotNull
    private Long facilityId;
    @NotNull
    private LocalDate bookingDate;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
}
