package edu.java.clients;

import edu.java.responses.RepositoryResponse;
import edu.java.responses.GitHubUserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GitHubClient implements GitHubClientInterface {
    private  final WebClient webClient;

    public GitHubClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        System.out.println(baseUrl);
    }

    @Override
    public GitHubUserResponse fetchUser(String user) {
        return this.webClient.get().uri("/users/{user}", user).retrieve().bodyToMono(GitHubUserResponse.class).block();
    }

    @Override
    public RepositoryResponse fetchRepository(String user, String repo){
        return this.webClient.get().uri("/repos/{user}/{repo}", user, repo).retrieve().
            bodyToMono(RepositoryResponse.class).block();
    }

}
