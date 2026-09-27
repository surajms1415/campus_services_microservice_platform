package com.campusservices.notification.controller;

import com.campusservices.notification.dto.NotificationDto;
import com.campusservices.notification.dto.NotificationRequest;
import com.campusservices.notification.security.CustomAuthenticationToken;
import com.campusservices.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(@Valid @RequestBody NotificationRequest req, CustomAuthenticationToken auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        // Only allow admins (or internal services if configured) or the user themselves to send notifications to their own ID
        if (!isAdmin && !auth.getUserId().equals(req.getUserId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(service.createNotification(req), HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<NotificationDto>> getMyNotifications(CustomAuthenticationToken auth) {
        return ResponseEntity.ok(service.getMyNotifications(auth.getUserId()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id, CustomAuthenticationToken auth) {
        return ResponseEntity.ok(service.markAsRead(id, auth.getUserId()));
    }
}
