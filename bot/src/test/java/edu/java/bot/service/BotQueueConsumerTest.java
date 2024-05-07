package edu.java.bot.service;

import dto.LinkUpdateRequest;
import java.net.URI;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootApplication
class BotQueueConsumerTest {
    @Mock
    private UpdateService updateService;
    @InjectMocks
    private BotQueueConsumer botQueueConsumer;

    public BotQueueConsumerTest() {
        MockitoAnnotations.openMocks(this);
        botQueueConsumer = new BotQueueConsumer(updateService);
    }

    @Test
    void listen() {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1L,
            URI.create("http://localhost"),
            "test updates",
            Arrays.asList(1L, 2L, 3L)
        );

        botQueueConsumer.listen(linkUpdateRequest);

        verify(updateService, times(1)).sendUpdate(linkUpdateRequest);
    }
}
