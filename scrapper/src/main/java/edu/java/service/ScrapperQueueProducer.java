package edu.java.service;

import dto.LinkUpdateRequest;
import edu.java.configuration.TopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
public class ScrapperQueueProducer implements NotificationSender {

    private final KafkaTemplate<String, LinkUpdateRequest> linkProducer;

    private final TopicProperties topic;

    @Override
    public void send(LinkUpdateRequest linkUpdateRequest) {
        try {
            log.info(topic.name());
            linkProducer.send(topic.name(), linkUpdateRequest);
        } catch (Exception ex) {
            log.info("Error with sending message to Kafka: ", ex);
        }
    }
}
