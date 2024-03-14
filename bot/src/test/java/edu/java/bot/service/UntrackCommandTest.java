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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class UntrackCommandTest {
    @Mock
    private Bot bot;

    @InjectMocks
    private UntrackCommand untrackCommand;

    Update update = mock(Update.class);
    Chat chat = mock(Chat.class);
    Message message = mock(Message.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        untrackCommand = spy(new UntrackCommand(null));
    }

    @Test
    void testHandleUntrackCommand() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/untrack");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        doReturn("someUrl").when(bot).waitingNewMessage(any());

        SendMessage result = untrackCommand.handleCommand(update, bot);

        SendMessage unexpectedMessage = new SendMessage(chat.id(), "Неверная команда!");
        assertNotEquals(unexpectedMessage.getParameters(), result.getParameters());
    }

    @Test
    void testHandleNotUntrackCommand() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/not untrack");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        doReturn("someUrl").when(bot).waitingNewMessage(any());

        SendMessage result = untrackCommand.handleCommand(update, bot);

        SendMessage expectedMessage = new SendMessage(chat.id(), "Неверная команда!");
        assertEquals(expectedMessage.getParameters(), result.getParameters());
    }

    @Test
    void testHandleUntrackCommandEmptyList() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/untrack");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        doReturn(Collections.emptyList())
            .when(bot)
            .getListOfURLS(anyLong());

        SendMessage result = untrackCommand.handleCommand(update, bot);

        SendMessage expectedMessage = new SendMessage(
            chat.id(),
            "Список ссылок пуст. Для удаления ссылки список не должен быть пустым."
        );
        assertEquals(result.getParameters(), expectedMessage.getParameters());
    }

    @Test
    void testHandleDeleteExistURL() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        String urlStr = "http://example.com";
        doReturn(true).when(bot).containsURL(anyLong(), any());

        SendMessage result = untrackCommand.handleDeleteUrl(update, urlStr, bot);

        SendMessage expectedMessage = new SendMessage(chat.id(), "Ссылка была удалена из списка.");
        assertEquals(expectedMessage.getParameters(), result.getParameters());
    }

    @Test
    void testHandleDeleteNotExistURL() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        String urlStr = "http://example.com";
        doReturn(false).when(bot).containsURL(anyLong(), any());

        SendMessage result = untrackCommand.handleDeleteUrl(update, urlStr, bot);

        SendMessage expectedMessage = new SendMessage(chat.id(), "Данной ссылки нет в списке.");
        assertEquals(expectedMessage.getParameters(), result.getParameters());
    }

    @Test
    void testHandleDeleteNotURL() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        String urlStr = "some url";

        SendMessage result = untrackCommand.handleDeleteUrl(update, urlStr, bot);

        SendMessage expectedMessage = new SendMessage(
            chat.id(),
            "Неверна указана ссылка. Для отправки ссылки введите команду /track"
        );
        assertEquals(result.getParameters(), expectedMessage.getParameters());
    }
}
