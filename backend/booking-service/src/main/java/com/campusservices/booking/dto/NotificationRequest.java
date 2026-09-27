package com.campusservices.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationRequest {
    private Long userId;
    private String message;
    private String type;
}
