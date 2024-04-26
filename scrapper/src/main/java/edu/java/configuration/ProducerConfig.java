package edu.java.configuration;

import dto.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class ProducerConfig {
    @Bean
    public NewTopic topic(TopicProperties topicProperties) {
        return TopicBuilder.name(topicProperties.name())
            .partitions(topicProperties.partitions())
            .replicas(topicProperties.replicas())
            .build();
    }

    @Bean
    public ProducerFactory<String, LinkUpdateRequest> producerFactory(KafkaProducerProperties properties) {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            properties.bootstrapServers());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG, properties.clientId());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, properties.ackMode());
        props.put(
            org.apache.kafka.clients.producer.ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
            (int) properties.deliveryTimeout().toMillis()
        );
        props.put(org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG, properties.lingerMs());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG, properties.batchSize());
        props.put(
            org.apache.kafka.clients.producer.ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
            properties.maxInFlightPerConnection()
        );
        props.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
            properties.enableIdempotence());
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> linkProducer(
        ProducerFactory<String, LinkUpdateRequest> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}
