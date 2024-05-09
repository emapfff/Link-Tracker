package edu.java.service;

import dto.LinkUpdateRequest;
import edu.java.configuration.TopicProperties;
import java.net.URI;
import java.util.Arrays;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ScrapperQueueProducerTest {
    @Mock
    private KafkaTemplate<String, LinkUpdateRequest> linkProducer;
    @Mock
    private Counter messageCounter;

    private ScrapperQueueProducer scrapperQueueProducer;
    @Captor
    private ArgumentCaptor<String> topicCaptor;

    @Captor
    private ArgumentCaptor<LinkUpdateRequest> requestCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scrapperQueueProducer = new ScrapperQueueProducer(linkProducer,
            new TopicProperties("topicName", 5, 1),  messageCounter);
    }

    @Test
    void sendTrueTest() {
        //true message
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            123L,
            URI.create("http://mycore"),
            "updating link",
            Arrays.asList(1L, 2L, 3L)
        );

        scrapperQueueProducer.send(linkUpdateRequest);

        verify(linkProducer, times(1)).send(topicCaptor.capture(), requestCaptor.capture());
        Assertions.assertEquals(topicCaptor.getValue(), "topicName");
        Assertions.assertEquals(requestCaptor.getValue(), linkUpdateRequest);
    }

}

