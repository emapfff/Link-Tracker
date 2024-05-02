package edu.java.bot.service;

import edu.java.bot.dto.LinkUpdateRequest;
import java.net.URI;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.listener.CommonErrorHandler;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootApplication
class BotQueueConsumerTest {
    @Value("${kafka.dlq.topic}")
    String dlqTopic;
    @Mock
    private CommonErrorHandler errorHandler;
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

    @Test
    void invalidLink() {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(null, null, null, null);

        RuntimeException exception =
            assertThrows(RuntimeException.class, () -> botQueueConsumer.listen(linkUpdateRequest));
        assertEquals("Invalid link update request", exception.getMessage());
    }

}
