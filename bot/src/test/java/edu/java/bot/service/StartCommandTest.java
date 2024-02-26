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
import static org.mockito.Mockito.*;
class StartCommandTest {
    @Mock
    private Bot bot;

    @InjectMocks
    private StartCommand startCommand;


    Update update = mock(Update.class);
    Chat chat = mock(Chat.class);
    Message message = mock(Message.class);

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        startCommand = new StartCommand(null);
    }
    @Test
    public void testHandleStartCommand(){
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        doNothing().when(bot).addUser(anyLong());
        SendMessage result = startCommand.handleCommand(update, bot);
        SendMessage expectedMessage = new SendMessage(update.message().chat().id(), "Добро пожаловать в Worker бота, null null!");
        verify(bot).addUser(123456789L);
        assertEquals(result.getParameters(), expectedMessage.getParameters());
    }

    @Test
    public void testHandleNoStartCommand(){
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/no start");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        doNothing().when(bot).addUser(anyLong());
        SendMessage result = startCommand.handleCommand(update, bot);
        SendMessage expectedMessage = new SendMessage(update.message().chat().id(), "Неверная команда!");
        assertEquals(result.getParameters(), expectedMessage.getParameters());
    }


}
