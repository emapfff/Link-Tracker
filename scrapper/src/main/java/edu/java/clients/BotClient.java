package edu.java.clients;

import dto.LinkUpdateRequest;
import edu.java.configuration.ClientConfig;
import edu.java.configuration.RetryBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class BotClient {
    private final WebClient botWebClient;

    private final ClientConfig clientConfig;

    private final RetryBuilder retryBuilder;

    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
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
