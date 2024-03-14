package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class})
public class BotApplication {
    private final Bot bot;

    @Autowired
    public BotApplication(ApplicationConfig applicationConfig) {
        this.bot = new Bot(applicationConfig);
    }

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

}
