package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Bot;
import edu.java.bot.configuration.ApplicationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotTest {
    private Bot bot;

    @Mock
    private ApplicationConfig applicationConfig;

    @BeforeEach
    public void setUp(){
        bot = new Bot(applicationConfig);
    }

    @Test
    public void testAddUser() {
        Long userId = 123456789L;

        bot.addUser(userId);

        assertTrue(bot.references.containsKey(userId));
    }

    @Test
    public void testAddURLs() throws MalformedURLException {
        Long userId = 123456789L;
        bot.addUser(userId);
        URL url1 = new URL("http://example.com/1");
        URL url2 = new URL("http://example.com/2");
        bot.addURLs(userId, url1);
        bot.addURLs(userId, url2);

        List<URL> listOfURLs = bot.getListOfURLS(userId);

        assertEquals(2, listOfURLs.size());
        assertTrue(listOfURLs.contains(url1));
        assertTrue(listOfURLs.contains(url2));
    }

    @Test
    public void testDeleteURLs() throws MalformedURLException {
        Long userId = 12345L;
        bot.addUser(userId);
        URL url1 = new URL("http://example.com/1");
        URL url2 = new URL("http://example.com/2");

        bot.addURLs(userId, url1);
        bot.addURLs(userId, url2);
        bot.deleteURLs(userId, url1);
        List<URL> urls = bot.getListOfURLS(userId);

        assertEquals(1, urls.size());
        assertFalse(urls.contains(url1));
        assertTrue(urls.contains(url2));
    }

    @Test
    public void testContainsURL() throws MalformedURLException {
        Long userId = 123456789L;
        URL url1 = new URL("http://example.com/1");
        URL url2 = new URL("http://example.com/2");

        bot.references.put(userId, Arrays.asList(url1, url2));
        boolean containsURL1 = bot.containsURL(userId, url1);
        boolean containsURL2 = bot.containsURL(userId, new URL("http://example.com/3")); // URL not in the list

        assertTrue(containsURL1);
        assertFalse(containsURL2);
    }

    @Test
    public void testExecuteCommand() {
        Long userId = 123456789L;
        TelegramBot telegramBotMock = Mockito.mock(TelegramBot.class);
        bot.setTelegramBot(telegramBotMock);
        SendMessage sendMessage = new SendMessage(userId, "Hello");

        bot.executeCommand(sendMessage);

        Mockito.verify(bot.getTelegramBot()).execute(sendMessage);
    }
}
