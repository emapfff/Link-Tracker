package edu.java.clients;

import edu.java.responses.QuestionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class StackOverflowClient {
    private final WebClient stackoverflowClient;

    public Mono<QuestionResponse> fetchQuestion(long id) {
        return this.stackoverflowClient
            .get()
            .uri("/questions/{id}?site=stackoverflow", id)
            .retrieve()
            .bodyToMono(QuestionResponse.class);
    }

}
