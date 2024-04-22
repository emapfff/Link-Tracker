package edu.java.clients;

import edu.java.configuration.ClientConfig;
import edu.java.response.BranchResponse;
import edu.java.response.GitHubUserResponse;
import edu.java.response.ListBranchesResponse;
import edu.java.response.RepositoryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(ClientConfig.class)
public class GitHubClient {
    private final WebClient githubClient;
    private final Retry retry;

    public Mono<GitHubUserResponse> fetchUser(String user) {
        return this.githubClient
            .get()
            .uri("/users/{user}", user)
            .retrieve()
            .bodyToMono(GitHubUserResponse.class)
            .retryWhen(retry);
    }

    public Mono<RepositoryResponse> fetchRepository(String user, String repo) {
        return this.githubClient
            .get()
            .uri("/repos/{user}/{repo}", user, repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .retryWhen(retry);
    }

    public Mono<ListBranchesResponse> fetchBranch(String user, String repo) {
        return this.githubClient
            .get()
            .uri("/repos/{user}/{repo}/branches", user, repo)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<BranchResponse>>() {
            })
            .map(ListBranchesResponse::new)
            .retryWhen(retry);
    }
}
