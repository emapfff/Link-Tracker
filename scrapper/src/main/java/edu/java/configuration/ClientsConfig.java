package edu.java.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "clients", ignoreUnknownFields = false)
public record ClientsConfig(
    @NotEmpty
    @Bean
    String github_base_url,
    String stackoverflow_base_url
) {}
