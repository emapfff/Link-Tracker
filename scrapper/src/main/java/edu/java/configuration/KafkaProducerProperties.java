package edu.java.configuration;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kafka.producer")
public record KafkaProducerProperties(
    String bootstrapServers,
    String clientId,
    String ackMode,
    Duration deliveryTimeout,
    Integer lingerMs,
    Integer batchSize,
    Integer maxInFlightPerConnection,
    Boolean enableIdempotence
) {
}
