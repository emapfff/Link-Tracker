package edu.java.bot;

import edu.java.bot.service.Bot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("edu.java.bot.configuration")
@RequiredArgsConstructor
public class BotApplication {
    private final Bot bot;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

    @PostConstruct
    private void startBot() {
        bot.start();
    }
}
