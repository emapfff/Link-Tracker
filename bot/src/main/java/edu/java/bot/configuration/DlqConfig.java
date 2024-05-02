package edu.java.bot.configuration;

import com.google.gson.JsonSerializer;
import edu.java.bot.dto.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
public class DlqConfig {
    @Bean
    public ProducerFactory<String, LinkUpdateRequest> dlqProducer(DlqProps dlqProps) {
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
    public DeadLetterPublishingRecoverer recover(DlqProps dlqProps) {
        return new DeadLetterPublishingRecoverer(
            dlq(dlqProducer(dlqProps)),
            (r, e) -> new TopicPartition(dlqProps.topic(), dlqProps.partitions())
        );
    }

    @Bean
    public CommonErrorHandler errorHandler(DeadLetterPublishingRecoverer recover) {
        return new DefaultErrorHandler(recover);
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> dlq(
        ProducerFactory<String, LinkUpdateRequest> dlqProducer
    ) {
        return new KafkaTemplate<>(dlqProducer);
    }
}
