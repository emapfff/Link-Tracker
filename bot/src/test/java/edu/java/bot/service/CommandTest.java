package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandTest {
    private Command command;
    @Mock
    private Bot bot;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        command = new Command(null){
            @Override
            protected String getCommand(Update update){
                return "test command";
            }
            @Override
            protected Long getUserId(Update update){
                return 123456L;
            }
            @Override
            protected Long getChatId(Update update){
                return 223L;
            }

            @Override
            protected String getUserName(Update update){
                return "Test user";
            }
        };
    }

    @Test
    public void testHandleCommand(){
        Update update = mock(Update.class);
        SendMessage result = command.handleCommand(update, bot);
        SendMessage expectedMessage = new SendMessage(command.getChatId(update), "Неверная команда!");
        assertEquals(result.getParameters(),expectedMessage.getParameters());
    }


}
