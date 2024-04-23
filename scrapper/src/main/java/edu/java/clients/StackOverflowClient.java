package edu.java.clients;

import edu.java.configuration.ClientConfig;
import edu.java.configuration.RetryBuilder;
import edu.java.response.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StackOverflowClient {
    private final WebClient stackoverflowClient;

    private final ClientConfig clientConfig;

    private final RetryBuilder retryBuilder;

    public Mono<QuestionResponse> fetchQuestion(long id) {
        return this.stackoverflowClient
            .get()
            .uri("/questions/{id}?site=stackoverflow", id)
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .retryWhen(retryBuilder.getRetry(clientConfig.stackoverflow().retryPolicy()));
    }

}
