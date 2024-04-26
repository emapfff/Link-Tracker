package edu.java.bot.service;

import dto.LinkUpdateRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BotQueueConsumerTest {

    @Mock
    private UpdateService updateService;

    private BotQueueConsumer botQueueConsumer;

    public BotQueueConsumerTest() {
        MockitoAnnotations.openMocks(this);
        botQueueConsumer = new BotQueueConsumer(updateService);
    }

    @Test
    void listen() {
        LinkUpdateRequest linkUpdateRequest = mock(LinkUpdateRequest.class);

        botQueueConsumer.listen(linkUpdateRequest);

        verify(updateService, times(1)).sendUpdate(linkUpdateRequest);

    }
}
