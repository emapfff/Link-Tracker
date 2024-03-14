package edu.java.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class ListCommandTest {
    @Mock
    private Bot bot;

    @InjectMocks
    private ListCommand listCommand;


    Update update = mock(Update.class);
    Message message = mock(Message.class);
    Chat chat = mock(Chat.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        listCommand = new ListCommand(null);
    }

    @Test
    void testHandleListCommand() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);

        SendMessage result = listCommand.handleCommand(update, bot);

        SendMessage unexpectedMessage = new SendMessage(chat.id(), "Неверная команда!");
        assertNotEquals(unexpectedMessage, result);
    }

    @Test
    void testHandleNotLitCommand() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/no list");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);

        SendMessage result = listCommand.handleCommand(update, bot);

        SendMessage expectedMessage = new SendMessage(chat.id(), "Неверная команда!");
        assertNotEquals(expectedMessage, result);
    }

    @Test
    void testHandleEmptyList() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        List<URL> urlList = new ArrayList<>();
        doReturn(urlList)
            .when(bot)
            .getListOfURLS(anyLong());

        SendMessage result = listCommand.handleCommand(update, bot);

        SendMessage expectedMessage = new SendMessage(chat.id(), "Список ссылок пуст.");
        assertEquals(result.getParameters(), expectedMessage.getParameters());
    }

    @Test
    void testHandleNotEmptyList() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        List<URL> urlList = new ArrayList<>();
        try {
            urlList.add(new URL("https://example.com"));
            urlList.add(new URL("https://example.org"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        doReturn(urlList)
            .when(bot)
            .getListOfURLS(anyLong());

        SendMessage result = listCommand.handleCommand(update, bot);

        SendMessage unexpectedMessage = new SendMessage(chat.id(), "Список ссылок пуст.");
        assertNotEquals(result.getParameters(), unexpectedMessage.getParameters());
    }
}
