package edu.java.configuration;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ClientsConfig.class)
public  class ClientConfiguration {
    private final ClientsConfig config;

    public ClientConfiguration(ClientsConfig clientsConfig) {
        this.config = clientsConfig;
    }
    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClient(config.github_base_url());
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(config.stackoverflow_base_url());
    }
}
