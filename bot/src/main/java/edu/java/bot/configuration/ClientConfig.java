package edu.java.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

@Validated
@ConfigurationProperties(prefix = "clients", ignoreUnknownFields = false)
public record ClientConfig(
    @Bean
    Scrapper scrapper
) {

    @Bean
    public WebClient scrapperWebClient() {
        return WebClient
            .builder()
            .baseUrl(scrapper.baseUrl())
            .build();
    }

    public record Scrapper(String baseUrl, Path path, Header header, RetryPolicy retryPolicy) {}

    public record Path(
        String link,
        String tgChat
    ) {
    }

    public record Header(
        String chatId
    ) {
    }

}
