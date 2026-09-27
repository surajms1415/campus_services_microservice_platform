package com.campusservices.notification.dto;

import com.campusservices.notification.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequest {
    @NotNull
    private Long userId;
    @NotBlank
    private String message;
    @NotNull
    private Notification.Type type;
}
