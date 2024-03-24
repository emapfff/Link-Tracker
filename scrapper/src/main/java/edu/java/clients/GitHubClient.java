package edu.java.clients;

import edu.java.responses.BranchResponse;
import edu.java.responses.GitHubUserResponse;
import edu.java.responses.RepositoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class GitHubClient {
    private final WebClient githubClient;

    public Mono<GitHubUserResponse> fetchUser(String user) {
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

    public Flux<BranchResponse> fetchBranch(String user, String repo) {
        return this.githubClient
            .get()
            .uri("/repos/{user}/{repo}/branches", user, repo)
            .retrieve()
            .bodyToFlux(BranchResponse.class);
    }
}
