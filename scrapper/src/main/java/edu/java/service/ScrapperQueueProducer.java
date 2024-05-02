package edu.java.service;

import edu.java.configuration.TopicProperties;
import edu.java.dto.LinkUpdateRequest;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
public class ScrapperQueueProducer implements NotificationSender {
    private final KafkaTemplate<String, LinkUpdateRequest> linkProducer;
    private final TopicProperties topic;
    private final Counter messageCounter;

    @Override
    public void send(LinkUpdateRequest linkUpdateRequest) {
        log.debug("KAFKA!");
        try {
            log.info(topic.name());
            messageCounter.increment();
            linkProducer.send(topic.name(), linkUpdateRequest);
        } catch (Exception ex) {
            log.info("Error with sending message to Kafka: ", ex);
        }
    }
}
