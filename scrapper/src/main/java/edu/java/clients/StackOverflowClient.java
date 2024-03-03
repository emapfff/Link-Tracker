package edu.java.clients;

import edu.java.configuration.ClientsConfig;
import edu.java.responses.QuestionResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@EnableConfigurationProperties(ClientsConfig.class)
@AllArgsConstructor
public class StackOverflowClient {
    private final WebClient stackOverflowClient;

    public Mono<QuestionResponse> fetchQuestion(long id) {
        return this.stackOverflowClient.get().uri("/questions/{id}?site=stackoverflow", id)
            .retrieve().bodyToMono(QuestionResponse.class);
    }

}
