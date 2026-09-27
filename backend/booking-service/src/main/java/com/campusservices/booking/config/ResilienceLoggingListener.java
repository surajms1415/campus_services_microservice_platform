package com.campusservices.booking.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceLoggingListener {
    private static final Logger log = LoggerFactory.getLogger(ResilienceLoggingListener.class);
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;

    public ResilienceLoggingListener(CircuitBreakerRegistry circuitBreakerRegistry, RetryRegistry retryRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.retryRegistry = retryRegistry;
    }

    @PostConstruct
    public void setupEventLogging() {
        circuitBreakerRegistry.circuitBreaker("facilityService").getEventPublisher()
                .onStateTransition(event -> log.info("CIRCUIT BREAKER STATE TRANSITION: from {} to {}",
                        event.getStateTransition().getFromState(),
                        event.getStateTransition().getToState()));

        retryRegistry.retry("facilityService").getEventPublisher()
                .onRetry(event -> log.warn("RETRY ATTEMPT #{} for Facility Service", event.getNumberOfRetryAttempts()))
                .onError(event -> log.error("RETRY FAILED after {} attempts", event.getNumberOfRetryAttempts()));
    }
}
