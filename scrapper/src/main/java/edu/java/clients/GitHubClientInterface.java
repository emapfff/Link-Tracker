package edu.java.clients;

import edu.java.responses.GitHubUserResponse;
import edu.java.responses.RepositoryResponse;

public interface GitHubClientInterface {
    GitHubUserResponse fetchUser(String user);

    RepositoryResponse fetchRepository(String user, String repo);
}
