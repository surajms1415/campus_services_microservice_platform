package com.campusservices.booking.client;

import com.campusservices.booking.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NotificationServiceClient {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceClient.class);
    private final RestClient restClient;
    
    @Value("${services.notification.url}")
    private String notifServiceUrl;

    public NotificationServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void sendNotification(Long userId, String message, String type, String token) {
        try {
            NotificationRequest req = new NotificationRequest(userId, message, type);
            restClient.post()
                    .uri(notifServiceUrl + "/api/notifications")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(req)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Failed to send notification synchronously to user {}: {}", userId, e.getMessage());
        }
    }
}
