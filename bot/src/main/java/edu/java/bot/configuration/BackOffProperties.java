package edu.java.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "retry", ignoreUnknownFields = false)
public record BackOffProperties(
    @Value("${retry.back-off-type}")
    BackOffType backOffType,
    @Value("${retry.max-attempts}")
    int maxAttempts,
    @Value("${retry.initial-interval}")
    long initialInterval,
    @Value("${retry.multiplier}")
    int multiplier
) {
    public enum BackOffType {
        EXPONENTIAL,
        LINEAR,
        CONSTANT
    }
}

