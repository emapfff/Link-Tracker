package edu.java.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

@Validated
@ConfigurationProperties(prefix = "clients", ignoreUnknownFields = false)
public record ClientConfig(
    @Bean
    Github github,
    @Bean
    Stackoverflow stackoverflow,
    @Bean
    Bot bot
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
            .baseUrl(bot.baseUrl())
            .build();
    }

    public record Github(String baseUrl, RetryPolicy retryPolicy) {
    }

    public record Stackoverflow(String baseUrl, RetryPolicy retryPolicy) {
    }

    public record Bot(String baseUrl, RetryPolicy retryPolicy) {
    }
}
