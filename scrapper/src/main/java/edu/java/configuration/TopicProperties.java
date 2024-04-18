package edu.java.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kafka.topic")
public record TopicProperties(
    String name,
    Integer partitions,
    Integer replicas
){}
