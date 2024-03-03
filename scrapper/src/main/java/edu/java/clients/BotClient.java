package edu.java.clients;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sharedDTOs.LinkUpdateRequest;

@Service
public class BotClient {
    private final WebClient botClient;

    public BotClient(String baseUrl){
        this.botClient = WebClient.builder().baseUrl(baseUrl).build();
    }
    public Mono<Void> sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        return this.botClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(linkUpdateRequest))
            .retrieve()
            .bodyToMono(Void.class);
    }

}
