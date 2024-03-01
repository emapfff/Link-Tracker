package edu.java.clients;

import edu.java.configuration.ClientsConfig;
import edu.java.responses.GitHubUserResponse;
import edu.java.responses.RepositoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@EnableConfigurationProperties(ClientsConfig.class)
@RequiredArgsConstructor
public class GitHubClient {
    private final WebClient githubClient;

    public Mono<GitHubUserResponse> fetchUser(String user) {
        return this.githubClient.get().uri("/users/{user}", user)
            .retrieve()
            .bodyToMono(GitHubUserResponse.class);
    }

    public Mono<RepositoryResponse> fetchRepository(String user, String repo) {
        return this.githubClient.get().uri("/repos/{user}/{repo}", user, repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class);
    }

}
