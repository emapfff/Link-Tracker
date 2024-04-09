package edu.java.clients;

import edu.java.response.BranchResponse;
import edu.java.response.GitHubUserResponse;
import edu.java.response.RepositoryResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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

    public Mono<List<BranchResponse>> fetchBranch(String user, String repo) {
        return this.githubClient
            .get()
            .uri("/repos/{user}/{repo}/branches", user, repo)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}
