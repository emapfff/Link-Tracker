package edu.java.bot.configuration;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kafka.dlq", ignoreUnknownFields = false)
public record DlqProps(
    String topic,
    Integer partitions,
    String bootstrapServers,
    String ackMode,
    Duration deliveryTimeout,
    Integer lingerMs
) {
}
