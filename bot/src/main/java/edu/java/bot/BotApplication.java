package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    private static ApplicationConfig applicationConfig;

    @Autowired
    public BotApplication(ApplicationConfig applicationConfig) {
        BotApplication.applicationConfig = applicationConfig;
    }

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
        Bot telegramBot = new Bot(new TelegramBot(getToken()));
        telegramBot.processUpdate();
    }

    private static String getToken() {
        return applicationConfig.telegramToken();
    }
}
