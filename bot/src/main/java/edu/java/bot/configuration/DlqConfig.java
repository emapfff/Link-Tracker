package edu.java.bot.configuration;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
public class DlqConfig {
    @Bean
    public NewTopic dlqTopic(DlqProps dlqProps) {
        return TopicBuilder.name(dlqProps.topic())
            .partitions(dlqProps.partitions())
            .build();
    }

    @Bean
    public ProducerFactory<String, Object> dlqProducer(DlqProps dlqProps) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, dlqProps.bootstrapServers());
        props.put(ProducerConfig.ACKS_CONFIG, dlqProps.ackMode());
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, (int) dlqProps.deliveryTimeout().toMillis());
        props.put(ProducerConfig.LINGER_MS_CONFIG, dlqProps.lingerMs());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> dlq(
        ProducerFactory<String, Object> dlqProducer
    ) {
        return new KafkaTemplate<>(dlqProducer);
    }
}
