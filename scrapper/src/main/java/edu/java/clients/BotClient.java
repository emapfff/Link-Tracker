package edu.java.clients;

import dto.LinkUpdateRequest;
import edu.java.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@RequiredArgsConstructor
public class BotClient implements NotificationSender {
    private final WebClient botWebClient;
    private final Retry retry;

    @Override
    public void send(LinkUpdateRequest linkUpdateRequest) {
        this.botWebClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(linkUpdateRequest))
            .retrieve()
            .bodyToMono(Void.class)
            .retryWhen(retry)
            .block();
    }

}
