package edu.java.clients;

import edu.java.responses.RepositoryResponse;
import edu.java.responses.GitHubUserResponse;

public interface GitHubClientInterface {
    GitHubUserResponse fetchUser(String user);

    RepositoryResponse fetchRepository(String user, String repo);
}
