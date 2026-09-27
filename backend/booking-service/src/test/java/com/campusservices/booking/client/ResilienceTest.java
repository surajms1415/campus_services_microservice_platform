package com.campusservices.booking.client;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ResilienceTest {

    @Autowired
    private FacilityServiceClient facilityClient;

    @Autowired
    private CircuitBreakerRegistry registry;
    
    // We can't easily mock RestClient directly without complicated setup,
    // so in this test we verify that the fallback logic kicks in properly 
    // by passing a non-existent URL or mocking the underlying call.
    // Since this is a demonstration of resilience test structure:

    @Test
    void fallbackExecutesWhenServiceFails() {
        // Act & Assert
        // Assuming RestClient fails to connect to a dummy port
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            facilityClient.getFacility(999L, "dummy-token");
        });
        
        assertEquals("Facility availability check is currently unavailable. Please try again later.", ex.getMessage());
    }
}
