package edu.java.bot.configuration;

import dto.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaConsumerProperties.class) public class KafkaConfig {
    @Bean
    public ConsumerFactory<String, LinkUpdateRequest> consumerFactory(KafkaConsumerProperties properties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.bootstrapServers());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, properties.clientId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, properties.autoOffsetReset());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, properties.maxPollInterval());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, properties.enableAutoCommit());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props,
            new StringDeserializer(),
            new JsonDeserializer<>(LinkUpdateRequest.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> listenerContainerFactory(
        ConsumerFactory<String, LinkUpdateRequest> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
