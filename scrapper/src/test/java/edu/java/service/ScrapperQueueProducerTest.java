package edu.java.service;

import dto.LinkUpdateRequest;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonDeserializer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.deser.std.StringDeserializer;

@EmbeddedKafka
@SpringBootTest(properties = "kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScrapperQueueProducerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private KafkaMessageListenerContainer<String, LinkUpdateRequest> container;
    private BlockingQueue<ConsumerRecord<String, LinkUpdateRequest>> records;
    
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    @Autowired
    private NotificationSender queueProducer;

    @BeforeAll
    void setUp() {
        DefaultKafkaConsumerFactory<String, LinkUpdateRequest> consumerFactory =
            new DefaultKafkaConsumerFactory<>(consProps())
    }

    private Map<String, Object> consProps() {
        return Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
            ConsumerConfig.GROUP_ID_CONFIG, "consumer",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
            ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10",
            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );
    }

    @Test
    void send() {
    }
}
