package com.campusservices.booking.controller;

import com.campusservices.booking.dto.BookingDto;
import com.campusservices.booking.dto.BookingRequest;
import com.campusservices.booking.entity.Booking;
import com.campusservices.booking.security.CustomAuthenticationToken;
import com.campusservices.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest req, CustomAuthenticationToken auth) {
        try {
            return new ResponseEntity<>(bookingService.createBooking(req, auth.getUserId(), auth.getToken()), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingDto>> getMyBookings(CustomAuthenticationToken auth) {
        return ResponseEntity.ok(bookingService.getMyBookings(auth.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id, CustomAuthenticationToken auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(bookingService.getBookingById(id, auth.getUserId(), isAdmin));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id, CustomAuthenticationToken auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(bookingService.cancelBooking(id, auth.getUserId(), isAdmin, auth.getToken()));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<BookingDto> approveBooking(@PathVariable Long id, CustomAuthenticationToken auth) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, Booking.Status.APPROVED, auth.getToken()));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<BookingDto> rejectBooking(@PathVariable Long id, CustomAuthenticationToken auth) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, Booking.Status.REJECTED, auth.getToken()));
    }
}
