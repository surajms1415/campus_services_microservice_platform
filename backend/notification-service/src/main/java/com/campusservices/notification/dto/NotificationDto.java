package com.campusservices.notification.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.campusservices.notification.entity.Notification.Type;

@Data
public class NotificationDto {
    private Long id;
    private Long userId;
    private String message;
    private Type type;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
