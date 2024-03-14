package edu.java.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "clients", ignoreUnknownFields = false)
public record ClientsConfig(
    @Bean
    String githubBaseUrl,
    @Bean
    String stackoverflowBaseUrl,
    @Bean
    String botBaseUrl
) {}
