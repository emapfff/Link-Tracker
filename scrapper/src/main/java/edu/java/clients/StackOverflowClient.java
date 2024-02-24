package edu.java.clients;

import edu.java.responses.QuestionResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class StackOverflowClient implements StackOverflowInterface {
    private final WebClient webClient;

    public StackOverflowClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public QuestionResponse fetchQuestion(long id) {
        return this.webClient.get().uri("/questions/{id}?site=stackoverflow", id)
            .retrieve().bodyToMono(QuestionResponse.class).block();
    }

}
