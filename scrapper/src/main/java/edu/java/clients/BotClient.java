package edu.java.clients;

import dto.LinkUpdateRequest;
import edu.java.configuration.ClientConfig;
import edu.java.configuration.RetryBuilder;
import edu.java.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class BotClient implements NotificationSender {
    private final WebClient botWebClient;

    private final ClientConfig clientConfig;

    private final RetryBuilder retryBuilder;

    @Override
    public void send(LinkUpdateRequest linkUpdateRequest) {
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
