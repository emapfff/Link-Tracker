package edu.java.clients;

import dto.LinkUpdateRequest;
import edu.java.exceptions.IncorrectParametersException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
public class BotClient {
    private final WebClient botWebClient;
    private final Retry retry;

    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        if (linkUpdateRequest == null) {
            throw new IncorrectParametersException("Некорректные параметры запроса");
        }
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
