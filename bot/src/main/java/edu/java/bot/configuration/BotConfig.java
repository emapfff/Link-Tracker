package edu.java.bot.configuration;

import edu.java.bot.Bot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {
    @Bean
    public Bot bot(ApplicationConfig applicationConfig) {
        return new Bot(applicationConfig);
    }
}
