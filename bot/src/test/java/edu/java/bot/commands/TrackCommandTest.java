package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.tools.LinkParser;
import edu.java.bot.tools.Resource;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrackCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private LinkParser linkParser;
    @Mock
    private ScrapperClient scrapperClient;
    @InjectMocks
    private TrackCommand trackCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void trackSuccess() {
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(123L);
        when(message.text()).thenReturn("https://example.com");
        when(linkParser.parse(any())).thenReturn(Resource.GITHUB);
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create("https://example.com"));
        when(scrapperClient.addLink(123L, addLinkRequest)).thenReturn(Mono.empty());

        trackCommand.handle(update);

        verify(scrapperClient).addLink(eq(123L), any());
    }

    @Test
    void trackFailure() {
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(123L);
        when(message.text()).thenReturn("https://example.com");
        when(linkParser.parse(any())).thenReturn(Resource.INCORRECT_URL);
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create("https://example.com"));
        when(scrapperClient.addLink(123L, addLinkRequest)).thenReturn(Mono.empty());

        SendMessage result = trackCommand.handle(update);

        assertTrue(result.getParameters().containsValue("Указана неверная ссылка.\n"
            + "Требуются ссылки репозиториев Github или вопросов из StackOverflow"));

    }

}
