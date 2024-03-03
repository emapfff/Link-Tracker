package edu.java.clients;

import dto.LinkUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class BotClient {
    private final WebClient botWebClient;

    public Mono<Void> sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        return this.botWebClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(linkUpdateRequest))
            .retrieve()
            .bodyToMono(Void.class);
    }

}
