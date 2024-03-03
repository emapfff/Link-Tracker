package edu.java.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(ClientsConfig.class)
public class ClientConfiguration {
    @Bean
    public WebClient githubClient(ClientsConfig clientsConfig) {
        return WebClient
            .builder()
            .baseUrl(clientsConfig.githubBaseUrl())
            .build();
    }

    @Bean
    public WebClient stackoverflowClient(ClientsConfig clientsConfig) {
        return WebClient
            .builder()
            .baseUrl(clientsConfig.stackoverflowBaseUrl())
            .build();
    }

    @Bean
    public WebClient botWebClient(ClientsConfig clientsConfig) {
        return WebClient
            .builder()
            .baseUrl(clientsConfig.botBaseUrl())
            .build();
    }

}
