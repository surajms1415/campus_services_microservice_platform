package com.campusservices.booking.service;

import com.campusservices.booking.client.FacilityServiceClient;
import com.campusservices.booking.client.NotificationServiceClient;
import com.campusservices.booking.dto.BookingDto;
import com.campusservices.booking.dto.BookingRequest;
import com.campusservices.booking.dto.FacilityResponseDto;
import com.campusservices.booking.entity.Booking;
import com.campusservices.booking.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock private BookingRepository repository;
    @Mock private FacilityServiceClient facilityClient;
    @Mock private NotificationServiceClient notifClient;

    @InjectMocks private BookingService service;

    @Test
    void createBooking_Success() {
        // Arrange
        BookingRequest req = new BookingRequest();
        req.setFacilityId(1L);
        req.setBookingDate(LocalDate.now().plusDays(1));
        req.setStartTime(LocalTime.of(10, 0));
        req.setEndTime(LocalTime.of(11, 0));
        
        FacilityResponseDto facility = new FacilityResponseDto();
        facility.setAvailable(true);
        
        when(facilityClient.getFacility(1L, "token")).thenReturn(facility);
        when(repository.countOverlappingBookings(any(), any(), any(), any())).thenReturn(0L);
        
        Booking saved = new Booking();
        saved.setId(1L);
        saved.setStatus(Booking.Status.PENDING);
        when(repository.save(any(Booking.class))).thenReturn(saved);
        
        // Act
        BookingDto res = service.createBooking(req, 100L, "token");
        
        // Assert
        assertNotNull(res);
        assertEquals(Booking.Status.PENDING, res.getStatus());
        verify(notifClient, times(1)).sendNotification(eq(100L), any(), any(), eq("token"));
    }

    @Test
    void createBooking_FacilityUnavailable_ThrowsException() {
        // Arrange
        BookingRequest req = new BookingRequest();
        req.setFacilityId(1L);
        req.setBookingDate(LocalDate.now().plusDays(1));
        req.setStartTime(LocalTime.of(10, 0));
        req.setEndTime(LocalTime.of(11, 0));
        
        FacilityResponseDto facility = new FacilityResponseDto();
        facility.setAvailable(false); // Unavailable
        
        when(facilityClient.getFacility(1L, "token")).thenReturn(facility);
        
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.createBooking(req, 100L, "token"));
        assertEquals("Facility is not available for booking", ex.getMessage());
    }

    @Test
    void createBooking_Overlapping_ThrowsException() {
        // Arrange
        BookingRequest req = new BookingRequest();
        req.setFacilityId(1L);
        req.setBookingDate(LocalDate.now().plusDays(1));
        req.setStartTime(LocalTime.of(10, 0));
        req.setEndTime(LocalTime.of(11, 0));
        
        FacilityResponseDto facility = new FacilityResponseDto();
        facility.setAvailable(true);
        when(facilityClient.getFacility(1L, "token")).thenReturn(facility);
        when(repository.countOverlappingBookings(any(), any(), any(), any())).thenReturn(1L); // Overlap
        
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.createBooking(req, 100L, "token"));
        assertEquals("Facility is already booked for the selected time range", ex.getMessage());
    }

    @Test
    void cancelBooking_Unauthorized_ThrowsException() {
        // Arrange
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setUserId(200L); // Belongs to user 200
        
        when(repository.findById(1L)).thenReturn(Optional.of(booking));
        
        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.cancelBooking(1L, 100L, false, "token"));
        assertEquals("Unauthorized cancellation", ex.getMessage());
    }
}
