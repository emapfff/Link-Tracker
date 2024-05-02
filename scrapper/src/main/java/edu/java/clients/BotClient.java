package edu.java.clients;

import edu.java.configuration.ClientConfig;
import edu.java.configuration.RetryBuilder;
import edu.java.dto.LinkUpdateRequest;
import edu.java.service.NotificationSender;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
public class BotClient implements NotificationSender {
    private final WebClient botWebClient;

    private final ClientConfig clientConfig;

    private final RetryBuilder retryBuilder;

    private final Counter messageCounter;

    @Override
    public void send(LinkUpdateRequest linkUpdateRequest) {
        log.debug("HTTP!");
        messageCounter.increment();
        this.botWebClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(linkUpdateRequest))
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(retryBuilder.getRetry(clientConfig.bot().retryPolicy()))
            .block();
    }

}
