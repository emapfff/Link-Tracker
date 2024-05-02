package edu.java.configuration;

import edu.java.clients.BotClient;
import edu.java.dto.LinkUpdateRequest;
import edu.java.service.NotificationSender;
import edu.java.service.ScrapperQueueProducer;
import io.micrometer.core.instrument.Counter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Validated
@Slf4j
public class SenderConfig {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public NotificationSender queueProducer(
        KafkaTemplate<String, LinkUpdateRequest> linkProducer,
        TopicProperties topicProperties, Counter messageCounter
    ) {
        log.info("kafka");
        return new ScrapperQueueProducer(linkProducer, topicProperties, messageCounter);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public NotificationSender httpSender(WebClient botWebClient, ClientConfig clientConfig, RetryBuilder retryBuilder,
        Counter messageCounter) {
        log.info("http");
        return new BotClient(botWebClient, clientConfig, retryBuilder, messageCounter);
    }
}
