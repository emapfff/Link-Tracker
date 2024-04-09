package edu.java.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "retry", ignoreUnknownFields = false)
public record BackOffProperties(
    @NotNull
    BackOffType backOffType,
    Integer maxAttempts,
    Integer maxInterval,
    Integer initialInterval,
    Integer multiplier
) {
    public enum BackOffType {
        EXPONENTIAL,
        LINEAR,
        CONSTANT
    }
}
