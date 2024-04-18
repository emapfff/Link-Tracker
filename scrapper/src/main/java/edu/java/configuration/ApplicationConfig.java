package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NestedConfigurationProperty
    @Bean
    @NotNull
    Scheduler scheduler,
    AccessType databaseAccessType,
    boolean useQueue
) {
    public enum AccessType {
        JDBC,
        JPA,
        JOOQ
    }

    public record Scheduler(
        boolean enable,
        Duration interval,
        Duration forceCheckDelay) {
    }
}
