package com.campusservices.notification.service;

import com.campusservices.notification.dto.NotificationDto;
import com.campusservices.notification.dto.NotificationRequest;
import com.campusservices.notification.entity.Notification;
import com.campusservices.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public NotificationDto createNotification(NotificationRequest req) {
        Notification n = new Notification();
        n.setUserId(req.getUserId());
        n.setMessage(req.getMessage());
        n.setType(req.getType());
        n.setIsRead(false);
        return mapToDto(repository.save(n));
    }

    public List<NotificationDto> getMyNotifications(Long userId) {
        return repository.findByUserIdAndIsReadFalse(userId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public NotificationDto markAsRead(Long id, Long userId) {
        Notification n = repository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        if (!n.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        n.setIsRead(true);
        return mapToDto(repository.save(n));
    }

    private NotificationDto mapToDto(Notification n) {
        NotificationDto dto = new NotificationDto();
        dto.setId(n.getId());
        dto.setUserId(n.getUserId());
        dto.setMessage(n.getMessage());
        dto.setType(n.getType());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }
}
