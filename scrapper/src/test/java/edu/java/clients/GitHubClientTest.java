package edu.java.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.backoff.ConstantBackOff;
import edu.java.backoff.ExponentialBackOff;
import edu.java.backoff.LinearBackOff;
import edu.java.response.GitHubUserResponse;
import edu.java.response.ListBranchesResponse;
import edu.java.response.RepositoryResponse;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.resetAllScenarios;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {ExponentialBackOff.class, LinearBackOff.class, ConstantBackOff.class,
    RetryPolicy.class})
@WireMockTest(httpPort = 8080)
class GitHubClientTest {;
    @Autowired
    private ExponentialBackOff exponentialBackOff;
    @Autowired
    private LinearBackOff linearBackOff;
    @Autowired
    private ConstantBackOff constantBackOff;

    private Retry retry;

    private GitHubClient gitHubClient;

    @BeforeEach
    public void setUp() throws IOException {
        String branchesJson = FileUtils.readFileToString(
            new File("src/test/java/edu/java/json/Branches.json"),
            "UTF-8"
        );
        //4 attempts for retrying
        stubFor(get(urlEqualTo("/users/user")).inScenario("Check retry for user")
            .whenScenarioStateIs(STARTED)
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("2")
        );
        stubFor(get(urlEqualTo("/users/user")).inScenario("Check retry for user")
            .whenScenarioStateIs("2")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("3")
        );
        stubFor(get(urlEqualTo("/users/user")).inScenario("Check retry for user")
            .whenScenarioStateIs("3")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("4")
        );
        stubFor(get(urlEqualTo("/users/user")).inScenario("Check retry for user")
            .whenScenarioStateIs("4")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("5")
        );
        stubFor(get(urlEqualTo("/users/user")).inScenario("Check retry for user")
            .whenScenarioStateIs("5")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"login\" : \"Emil\", " +
                        "\"updated_at\" : \"2024-02-09T17:47:19Z\"}")
            ));

        stubFor(get(urlEqualTo("/repos/owner/repo")).inScenario("Check retry for repo")
            .whenScenarioStateIs(STARTED)
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("2")
        );
        stubFor(get(urlEqualTo("/repos/owner/repo")).inScenario("Check retry for repo")
            .whenScenarioStateIs("2")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("3")
        );
        stubFor(get(urlEqualTo("/repos/owner/repo")).inScenario("Check retry for repo")
            .whenScenarioStateIs("3")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("4")
        );
        stubFor(get(urlEqualTo("/repos/owner/repo")).inScenario("Check retry for repo")
            .whenScenarioStateIs("4")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("5")
        );
        stubFor(get(urlEqualTo("/repos/owner/repo")).inScenario("Check retry for repo")
            .whenScenarioStateIs("5")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-type", "application/json")
                    .withBody("{\"name\" : \"repo_name\", " +
                        "\"updated_at\" : \"2024-02-09T17:47:19Z\", " +
                        "\"owner:login\" : \"user\"}")
            ));

        stubFor(get(urlEqualTo("/repos/user/repo/branches")).inScenario("Check retry for branches")
            .whenScenarioStateIs(STARTED)
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("2")
        );
        stubFor(get(urlEqualTo("/repos/user/repo/branches")).inScenario("Check retry for branches")
            .whenScenarioStateIs("2")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("3")
        );
        stubFor(get(urlEqualTo("/repos/user/repo/branches")).inScenario("Check retry for branches")
            .whenScenarioStateIs("3")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("4")
        );
        stubFor(get(urlEqualTo("/repos/user/repo/branches")).inScenario("Check retry for branches")
            .whenScenarioStateIs("4")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("5")
        );
        stubFor(get(urlEqualTo("/repos/user/repo/branches")).inScenario("Check retry for branches")
            .whenScenarioStateIs("5")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(branchesJson)
            )
        );
    }

    @AfterEach
    public void stop() {
        resetAllScenarios();
    }

    @Test
    void fetchUserTestExponentialBackOff() {
        retry = exponentialBackOff;
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8080"), retry);

        GitHubUserResponse gitHubUserResponse = gitHubClient.fetchUser("user").block();

        assertNotNull(gitHubUserResponse);
        assertEquals(gitHubUserResponse.userName(), "Emil");
        assertEquals(gitHubUserResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchUserTestLinearBackOff() {
        retry = linearBackOff;
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8080"), retry);

        GitHubUserResponse gitHubUserResponse = gitHubClient.fetchUser("user").block();

        assertNotNull(gitHubUserResponse);
        assertEquals(gitHubUserResponse.userName(), "Emil");
        assertEquals(gitHubUserResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchUserTestConstantBackOff() {
        retry = constantBackOff;
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8080"), retry);

        GitHubUserResponse gitHubUserResponse = gitHubClient.fetchUser("user").block();

        assertNotNull(gitHubUserResponse);
        assertEquals(gitHubUserResponse.userName(), "Emil");
        assertEquals(gitHubUserResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchRepositoryTestExponentialBlackOff() {
        retry = exponentialBackOff;

        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8080"), retry);

        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository("owner", "repo").block();

        assertNotNull(repositoryResponse);
        assertEquals(repositoryResponse.repoName(), "repo_name");
        assertEquals(repositoryResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchRepositoryTestLinearBlackOff() {
        retry = linearBackOff;

        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8080"), retry);

        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository("owner", "repo").block();

        assertNotNull(repositoryResponse);
        assertEquals(repositoryResponse.repoName(), "repo_name");
        assertEquals(repositoryResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchRepositoryTestConstantBlackOff() {
        retry = constantBackOff;

        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8080"), retry);

        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository("owner", "repo").block();

        assertNotNull(repositoryResponse);
        assertEquals(repositoryResponse.repoName(), "repo_name");
        assertEquals(repositoryResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchBranchTestExponentialBlackOff() {
        retry = exponentialBackOff;
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8080"), retry);

        ListBranchesResponse branchResponses = gitHubClient.fetchBranch("user", "repo").block();

        assertNotNull(branchResponses);
        assertEquals(branchResponses.listBranches().size(), 2);
        assertEquals(branchResponses.listBranches().getFirst().name(), "hw1");
        assertEquals(branchResponses.listBranches().getLast().name(), "hw2");
    }

    @Test
    void fetchBranchTestLinearBlackOff() {
        retry = linearBackOff;
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8080"), retry);

        ListBranchesResponse branchResponses = gitHubClient.fetchBranch("user", "repo").block();

        assertNotNull(branchResponses);
        assertEquals(branchResponses.listBranches().size(), 2);
        assertEquals(branchResponses.listBranches().getFirst().name(), "hw1");
        assertEquals(branchResponses.listBranches().getLast().name(), "hw2");
    }

    @Test
    void fetchBranchTestConstantBlackOff() {
        retry = constantBackOff;
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8080"), retry);

        ListBranchesResponse branchResponses = gitHubClient.fetchBranch("user", "repo").block();

        assertNotNull(branchResponses);
        assertEquals(branchResponses.listBranches().size(), 2);
        assertEquals(branchResponses.listBranches().getFirst().name(), "hw1");
        assertEquals(branchResponses.listBranches().getLast().name(), "hw2");
    }
}

