package edu.java.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Validated
@EnableConfigurationProperties(BaseUrlConfig.class)
public class ClientConfig {
    @Bean
    public WebClient githubClient(BaseUrlConfig baseUrlConfig) {
        return WebClient
            .builder()
            .baseUrl(baseUrlConfig.githubBaseUrl())
            .build();
    }

    @Bean
    public WebClient stackoverflowClient(BaseUrlConfig baseUrlConfig) {
        return WebClient
            .builder()
            .baseUrl(baseUrlConfig.stackoverflowBaseUrl())
            .build();
    }

    @Bean
    public WebClient botWebClient(BaseUrlConfig baseUrlConfig) {
        return WebClient
            .builder()
            .baseUrl(baseUrlConfig.botBaseUrl())
            .build();
    }

}
