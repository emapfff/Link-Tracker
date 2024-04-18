package edu.java.service;

import dto.LinkUpdateRequest;
import edu.java.configuration.TopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer implements NotificationSender {

    private final KafkaTemplate<Integer, LinkUpdateRequest> linkProducer;

    private final TopicProperties topic;

    @Override
    public void send(LinkUpdateRequest linkUpdateRequest) {
        try {
            linkProducer.send(topic.name(), linkUpdateRequest);
        } catch (Exception ex) {
            log.info("Error with sending message to Kafka: ", ex);
        }
    }
}
