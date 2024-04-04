package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dto.AddLinkRequest;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.tools.LinkParse;
import edu.java.bot.tools.Urls;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrackCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private LinkParse linkParse;
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
        when(linkParse.parse(any())).thenReturn(Urls.GITHUB);
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
        when(linkParse.parse(any())).thenReturn(Urls.INCORRECT_URL);
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create("https://example.com"));
        when(scrapperClient.addLink(123L, addLinkRequest)).thenReturn(Mono.empty());

        SendMessage result = trackCommand.handle(update);

        assertTrue(result.getParameters().containsValue("Указана неверная ссылка.\n"
            + "Требуются ссылки репозитория Github или вопроса из StackOverflow"));

    }

}
