package com.example.order_service.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Configuration 
public class FoodCircuitBreakerConfig {

    @Bean 
    public CircuitBreaker foodCircuitBreaker(){

        CircuitBreakerConfig config =
                    CircuitBreakerConfig.custom()
                        .failureRateThreshold(50)
                        .slidingWindowSize(4)
                        .minimumNumberOfCalls(4)
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        .permittedNumberOfCallsInHalfOpenState(2)
                        .automaticTransitionFromOpenToHalfOpenEnabled(true)
                        .build();

                    
                    CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);

                    return registry.circuitBreaker("foodService");
        
    }

}
