package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.Bot;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
@AllArgsConstructor
public class BotApplication {
    @NotNull
    private final Bot bot;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

    @PostConstruct
    public void startBot() {
        bot.processUpdate();
    }
}
