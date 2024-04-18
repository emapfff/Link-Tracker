package edu.java.configuration;

import dto.LinkUpdateRequest;
import edu.java.clients.BotClient;
import edu.java.service.NotificationSender;
import edu.java.service.ScrapperQueueProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Configuration
@Validated
public class SenderConfig {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "true")
    public NotificationSender queueSender(
        KafkaTemplate<Integer, LinkUpdateRequest> linkProducer,
        TopicProperties topicProperties
    ) {
        return new ScrapperQueueProducer(linkProducer, topicProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "false")
    public NotificationSender httpSender(WebClient botWebClient, Retry retry) {
        return new BotClient(botWebClient, retry);
    }

}
