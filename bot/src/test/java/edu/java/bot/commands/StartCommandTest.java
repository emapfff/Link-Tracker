package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class StartCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private ScrapperClient scrapperClient;
    @InjectMocks
    private StartCommand startCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startSuccess() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);
        when(scrapperClient.registrationChat(1L)).thenReturn(Mono.empty());

        SendMessage expectedMessage = startCommand.handle(update);

        assertTrue(expectedMessage.getParameters().containsValue("Чат успешно добавлен"));
    }

    @Test
    void startFailure() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);
        when(scrapperClient.registrationChat(1L))
            .thenReturn(Mono.error(new RuntimeException("Чат был уже добавлен")));

        SendMessage expectedMessage = startCommand.handle(update);

        assertTrue(expectedMessage.getParameters().containsValue("Чат был уже добавлен"));
    }
}
