package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class})
@AllArgsConstructor
public class BotApplication {
    private final Bot bot;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
