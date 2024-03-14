package edu.java.bot.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(ScrapperClientProperties.class)
public class ScrapperClientConfiguration {
    @Bean
    public WebClient scrapperWebClient(ScrapperClientProperties scrapperClientProperties) {
        return WebClient
            .builder()
            .baseUrl(scrapperClientProperties.baseUrl())
            .build();
    }
}
