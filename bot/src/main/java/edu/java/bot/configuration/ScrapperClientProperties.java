package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "scrapper-client")
public record ScrapperClientProperties(
    @NotEmpty

    String baseUrl,
    Path path,
    Header header

) {
    public record Path(
        String link,
        String tgChat
    ) {}

    public record Header(
        String chatId
    ) {}
}
