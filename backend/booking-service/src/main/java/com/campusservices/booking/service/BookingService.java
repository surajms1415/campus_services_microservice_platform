package com.campusservices.booking.service;

import com.campusservices.booking.client.FacilityServiceClient;
import com.campusservices.booking.client.NotificationServiceClient;
import com.campusservices.booking.dto.BookingDto;
import com.campusservices.booking.dto.BookingRequest;
import com.campusservices.booking.dto.FacilityResponseDto;
import com.campusservices.booking.entity.Booking;
import com.campusservices.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository repository;
    private final FacilityServiceClient facilityClient;
    private final NotificationServiceClient notifClient;

    public BookingService(BookingRepository repository, FacilityServiceClient facilityClient, NotificationServiceClient notifClient) {
        this.repository = repository;
        this.facilityClient = facilityClient;
        this.notifClient = notifClient;
    }

    @Transactional
    public BookingDto createBooking(BookingRequest req, Long userId, String token) {
        if (req.getBookingDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Booking date cannot be in the past");
        }
        if (!req.getStartTime().isBefore(req.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        FacilityResponseDto facility = facilityClient.getFacility(req.getFacilityId(), token);
        if (facility == null) throw new IllegalArgumentException("Facility not found");
        if (!facility.getAvailable()) throw new IllegalArgumentException("Facility is not available for booking");

        long overlaps = repository.countOverlappingBookings(
                req.getFacilityId(), req.getBookingDate(), req.getStartTime(), req.getEndTime());
        if (overlaps > 0) throw new IllegalArgumentException("Facility is already booked for the selected time range");

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setFacilityId(req.getFacilityId());
        booking.setBookingDate(req.getBookingDate());
        booking.setStartTime(req.getStartTime());
        booking.setEndTime(req.getEndTime());
        booking.setStatus(Booking.Status.PENDING);

        Booking saved = repository.save(booking);
        notifClient.sendNotification(userId, "Your booking for facility " + req.getFacilityId() + " has been submitted and is PENDING approval.", "SYSTEM_ALERT", token);
        return mapToDto(saved);
    }

    public List<BookingDto> getMyBookings(Long userId) {
        return repository.findByUserId(userId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<BookingDto> getAllBookings() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public BookingDto getBookingById(Long id, Long userId, boolean isAdmin) {
        Booking booking = repository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!isAdmin && !booking.getUserId().equals(userId)) throw new RuntimeException("Unauthorized access");
        return mapToDto(booking);
    }

    @Transactional
    public BookingDto cancelBooking(Long id, Long userId, boolean isAdmin, String token) {
        Booking booking = repository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!isAdmin && !booking.getUserId().equals(userId)) throw new RuntimeException("Unauthorized cancellation");
        if (booking.getStatus() == Booking.Status.CANCELLED || booking.getStatus() == Booking.Status.REJECTED) {
            throw new IllegalArgumentException("Booking is already " + booking.getStatus());
        }
        booking.setStatus(Booking.Status.CANCELLED);
        Booking saved = repository.save(booking);
        notifClient.sendNotification(booking.getUserId(), "Your booking for facility " + booking.getFacilityId() + " has been CANCELLED.", "BOOKING_CANCELLATION", token);
        return mapToDto(saved);
    }

    @Transactional
    public BookingDto updateBookingStatus(Long id, Booking.Status status, String token) {
        Booking booking = repository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        Booking saved = repository.save(booking);
        
        String notifType = status == Booking.Status.APPROVED ? "BOOKING_CONFIRMATION" : "BOOKING_CANCELLATION";
        notifClient.sendNotification(booking.getUserId(), "Your booking for facility " + booking.getFacilityId() + " is now " + status + ".", notifType, token);
        
        return mapToDto(saved);
    }

    private BookingDto mapToDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUserId());
        dto.setFacilityId(booking.getFacilityId());
        dto.setBookingDate(booking.getBookingDate());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setStatus(booking.getStatus());
        dto.setCreatedAt(booking.getCreatedAt());
        return dto;
    }
}
