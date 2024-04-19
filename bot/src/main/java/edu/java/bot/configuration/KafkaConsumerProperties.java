package edu.java.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.consumer")
public record KafkaConsumerProperties(
    String bootstrapServers,
    String clientId,
    String autoOffsetReset,
    Integer maxPollInterval,
    Boolean enableAutoCommit
) {
}
