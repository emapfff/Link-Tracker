package edu.java.clients;

import edu.java.configuration.ClientConfig;
import edu.java.response.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(ClientConfig.class)
public class StackOverflowClient {
    private final WebClient stackoverflowClient;
    private final Retry retry;

    public Mono<QuestionResponse> fetchQuestion(long id) {
        return this.stackoverflowClient
            .get()
            .uri("/questions/{id}?site=stackoverflow", id)
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .retryWhen(retry);
    }

}
