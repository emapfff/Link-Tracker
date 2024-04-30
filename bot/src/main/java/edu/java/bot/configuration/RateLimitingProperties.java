package edu.java.bot.configuration;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "rate-limiting", ignoreUnknownFields = false)
public record RateLimitingProperties(
    Long capacity,

    Long refillRate,

    Long refillTimeSeconds,

    Long cacheSize,

    Duration expireAfterAccess

) {
}
