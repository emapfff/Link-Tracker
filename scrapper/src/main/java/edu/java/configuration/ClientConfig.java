package edu.java.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

@Validated
@ConfigurationProperties(prefix = "clients", ignoreUnknownFields = false)
public record ClientConfig(
    @NestedConfigurationProperty
        @NotNull
    Github github,
    Stackoverflow stackoverflow
) {

    @Bean
    public WebClient githubClient() {
        return WebClient
            .builder()
            .baseUrl(github.baseUrl())
            .build();
    }

    @Bean
    public WebClient stackoverflowClient() {
        return WebClient
            .builder()
            .baseUrl(stackoverflow.baseUrl())
            .build();
    }

    @Bean
    public WebClient botWebClient() {
        return WebClient
            .builder()
            .baseUrl("")
            .build();
    }

    public record Github(
        String baseUrl,
        RetryPolicy retryPolicy
    ) {
    }

    public record Stackoverflow(
        String baseUrl
    ) {
    }
}
