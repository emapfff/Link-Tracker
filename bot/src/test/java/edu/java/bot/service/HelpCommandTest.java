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
class HelpCommandTest {
    @Mock
    private Bot bot;
    @InjectMocks
    private HelpCommand helpCommand;

    Update update = mock(Update.class);
    Chat chat = mock(Chat.class);
    Message message = mock(Message.class);

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        helpCommand = new HelpCommand(null);
    }

    @Test
    public void testHandleHelpCommand(){
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/help");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        SendMessage result = helpCommand.handleCommand(update, bot);
        SendMessage expectedMessage = new SendMessage(update.message().chat().id(), """
                    /start -- зарегистрировать пользователя
                    /help -- вывести окно с командами
                    /track -- начать отслеживание ссылки
                    /untrack -- прекратить отслеживание ссылки
                    /list -- показать список отслеживаемых ссылок
                    """);
        assertEquals(result.getParameters(), expectedMessage.getParameters());
    }

    @Test
    public void testHandleNoHelpCommand(){
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/no help");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456789L);
        SendMessage result = helpCommand.handleCommand(update, bot);
        SendMessage expectedMessage = new SendMessage(update.message().chat().id(), "Неверная команда!");
        assertEquals(result.getParameters(), expectedMessage.getParameters());
    }
}
