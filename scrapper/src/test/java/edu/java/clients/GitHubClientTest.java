package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.responses.GitHubUserResponse;
import edu.java.responses.RepositoryResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.*;

class GitHubClientTest {

    private static WireMockServer wireMockServer;

    @BeforeEach
    public void setUp(){
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    public static void stop(){
        wireMockServer.stop();
    }
    @Test
    void fetchUserTest() {
        stubFor(get(urlEqualTo("/users/user"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"login\" : \"Emil\", " +
                        "\"updated_at\" : \"2024-02-09T17:47:19Z\"}")
            ));
        GitHubClient gitHubClient = new GitHubClient("http://localhost:" + wireMockServer.port());

        GitHubUserResponse gitHubUserResponse = gitHubClient.fetchUser("user");

        assertEquals(gitHubUserResponse.userName(), "Emil");
        assertEquals(gitHubUserResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchRepositoryTest(){
        stubFor(get(urlEqualTo("/repos/owner/repo"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-type", "application/json")
                    .withBody("{\"name\" : \"repo_name\", " +
                        "\"updated_at\" : \"2024-02-09T17:47:19Z\", " +
                        "\"owner:login\" : \"user\"}")
            ));
        GitHubClient gitHubClient = new GitHubClient("http://localhost:" + wireMockServer.port());

        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository("owner", "repo");

        assertEquals(repositoryResponse.repoName(), "repo_name");
        assertEquals(repositoryResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }
}

