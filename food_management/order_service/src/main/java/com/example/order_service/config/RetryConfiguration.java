package com.example.order_service.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

@Configuration
public class RetryConfiguration {

    @Bean
    public RetryRegistry retryRegistry() {

        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofSeconds(2))
                .retryExceptions(FeignException.class)
                .build();

        return RetryRegistry.of(config);
    }

    @Bean
    public Retry foodServiceRetry(RetryRegistry retryRegistry) {

        return retryRegistry.retry("foodserviceretry");
    }
}