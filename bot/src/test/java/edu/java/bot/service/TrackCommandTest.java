package edu.java.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TrackCommandTest {
    @Mock
    private Bot bot;

    @InjectMocks
    private TrackCommand trackCommand;

    Update update = mock(Update.class);
    Chat chat = mock(Chat.class);
    Message message = mock(Message.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trackCommand = spy(new TrackCommand(null));
    }

    @Test
    void testHandleTrackCommand() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/track");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        doReturn("someUrl").when(trackCommand).waitingNewMessage(any(), any());
        SendMessage result = trackCommand.handleCommand(update, bot);
        verify(bot).executeCommand(any());
        assertNotNull(result);
    }

    @Test
    void testHandleNoTrackCommand(){
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/no track");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        doReturn("someUrl").when(trackCommand).waitingNewMessage(any(), any());
        SendMessage result = trackCommand.handleCommand(update, bot);
        SendMessage expected = new SendMessage(update.message().chat().id(), "Неверная команда!");
        assertEquals(expected.getParameters(), result.getParameters());
    }

    @Test
    void testHandleCorrectURL(){
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);

        String url = "http://example.com";
        SendMessage result = trackCommand.handleAddUrl(update, url, bot);
        SendMessage unexpectedMessage = new SendMessage(
            update.message().chat().id(),
            "Неверна указана ссылка. Для отправки ссылки введите команду /track");
        assertNotEquals(unexpectedMessage.getParameters(), result.getParameters());

    }

    @Test
    void testHandleIncorrectURL(){
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);

        String url = "some";
        SendMessage result = trackCommand.handleAddUrl(update, url, bot);
        SendMessage expectedMessage = new SendMessage(
            update.message().chat().id(),
            "Неверна указана ссылка. Для отправки ссылки введите команду /track");
        assertEquals(expectedMessage.getParameters(), result.getParameters());
    }

    @Test
    void testHandleExistenceURL() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);

        String urlStr = "http://example.com";
        doReturn(true).when(bot).containsURL(any(), any());
        SendMessage result = trackCommand.handleAddUrl(update, urlStr, bot);
        SendMessage expected = new SendMessage(chat.id(), "Ссылка уже находится в списке.");
        assertEquals(result.getParameters(), expected.getParameters());
    }

    @Test
    void testHandleNewURL() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);

        String urlStr = "http://example.com";
        doReturn(false).when(bot).containsURL(any(), any());
        SendMessage result = trackCommand.handleAddUrl(update, urlStr, bot);
        SendMessage expected = new SendMessage(chat.id(), "Ссылка добавлена.");
        assertEquals(result.getParameters(), expected.getParameters());
    }

}
