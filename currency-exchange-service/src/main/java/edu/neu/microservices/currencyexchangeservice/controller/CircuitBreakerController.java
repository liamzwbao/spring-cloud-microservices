package edu.neu.microservices.currencyexchangeservice.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {
    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api-retry")
    @Retry(name = "sample-api-retry", fallbackMethod = "hardcodedResponse")
    public String sampleApiRetry() {
        logger.info("Sample Api call received");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class);
        return forEntity.getBody();
    }

    @GetMapping("/sample-api-circuit-breaker")
    @CircuitBreaker(name = "sample-api-circuit-breaker", fallbackMethod = "hardcodedResponse")
    @RateLimiter(name = "sample-api-rate-limiter")
    public String sampleApiCircuitBreaker() {
        logger.info("Sample Api call received");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class);
        return forEntity.getBody();
    }

    @GetMapping("/sample-api-bulkhead")
    @Bulkhead(name = "/sample-api-bulkhead")
    public String sampleApiBulkhead() {
        logger.info("Sample Api call received");
        return "sample-api";
    }

    private String hardcodedResponse(Exception ex) {
        return "fallback-response: " + ex.getMessage();
    }
}
