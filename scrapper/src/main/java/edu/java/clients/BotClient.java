package edu.java.clients;

import dto.LinkUpdateRequest;
import edu.java.exceptions.IncorrectParametersExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class BotClient {
    private final WebClient botWebClient;

    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        if (linkUpdateRequest == null) {
            throw new IncorrectParametersExceptions("Некорректные параметры запроса");
        }
        this.botWebClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(linkUpdateRequest))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

}
