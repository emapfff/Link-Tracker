package edu.java.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageProcessed {
    @Bean
    public Counter messageCounter(MeterRegistry meterRegistry) {
        return Counter.builder("processed_messages_total")
            .description("Total number of processed messages")
            .register(meterRegistry);
    }
}
