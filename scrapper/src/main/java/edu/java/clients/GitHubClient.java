package edu.java.clients;

import edu.java.responses.GitHubUserResponse;
import edu.java.responses.RepositoryResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class GitHubClient {
    private final WebClient githubClient;

    public Mono<GitHubUserResponse> fetchUser(String user) {
        log.info("get info about user");
        return this.githubClient
            .get()
            .uri("/users/{user}", user)
            .retrieve()
            .bodyToMono(GitHubUserResponse.class);
    }

    public Mono<RepositoryResponse> fetchRepository(String user, String repo) {
        return this.githubClient
            .get()
            .uri("/repos/{user}/{repo}", user, repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class);
    }

}
