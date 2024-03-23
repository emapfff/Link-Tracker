package edu.java.bot.commands;

import edu.java.bot.service.Bot;
import edu.java.bot.configuration.ApplicationConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotTest {
    private Bot bot;

    @Mock
    private ApplicationConfig applicationConfig;

}
