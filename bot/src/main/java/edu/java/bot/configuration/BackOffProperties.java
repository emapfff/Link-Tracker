package edu.java.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "retry", ignoreUnknownFields = false)
public record BackOffProperties(
    BackOffType backOffType,
    int maxAttempts,
    long initialInterval,
    int multiplier
) {
    public enum BackOffType {
        EXPONENTIAL,
        LINEAR,
        CONSTANT
    }
}

