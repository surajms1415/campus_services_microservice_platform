package com.campusservices.booking.client;

import com.campusservices.booking.dto.FacilityResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class FacilityServiceClient {
    private static final Logger log = LoggerFactory.getLogger(FacilityServiceClient.class);
    private final RestClient restClient;
    
    @Value("${services.facility.url}")
    private String facilityServiceUrl;

    public FacilityServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Retry(name = "facilityService")
    @CircuitBreaker(name = "facilityService", fallbackMethod = "facilityFallback")
    public FacilityResponseDto getFacility(Long facilityId, String token) {
        log.info("Attempting to fetch facility details for ID: {}", facilityId);
        try {
            return restClient.get()
                    .uri(facilityServiceUrl + "/api/facilities/" + facilityId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(FacilityResponseDto.class);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                // Return null so the service can throw "Facility not found" properly
                return null; 
            }
            throw e;
        }
    }

    public FacilityResponseDto facilityFallback(Long facilityId, String token, Throwable throwable) {
        log.warn("Fallback execution: Facility Service is down or unresponsive. Error: {}", throwable.getMessage());
        throw new RuntimeException("Facility availability check is currently unavailable. Please try again later.");
    }
}
