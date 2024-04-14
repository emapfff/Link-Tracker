package edu.java.configuration;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(
    String bootstrapServers,
    String clientId,
    String acksMode,
    Duration deliveryTimeout,
    Integer lingerMs,
    Integer batchSize,
    Integer maxInFlightPerConnection,
    Boolean enableIdempotence,
    String schemaRegistryUrl
) {
}
