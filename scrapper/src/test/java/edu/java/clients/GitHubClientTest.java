package edu.java.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.backoff.ConstantBackOff;
import edu.java.backoff.ExponentialBackOff;
import edu.java.backoff.LinearBackOff;
import edu.java.configuration.ClientConfig;
import edu.java.configuration.RetryBuilder;
import edu.java.configuration.RetryPolicy;
import edu.java.response.GitHubUserResponse;
import java.io.File;
import java.io.IOException;
import edu.java.response.ListBranchesResponse;
import edu.java.response.RepositoryResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.resetAllScenarios;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ExponentialBackOff.class, LinearBackOff.class, ConstantBackOff.class, RetryBuilder.class})
@WireMockTest(httpPort = 8001)
class GitHubClientTest {
    @Autowired
    RetryBuilder retryBuilder;
    @Mock
    private ClientConfig clientConfig;

    private Retry retry;

    @InjectMocks
    private GitHubClient gitHubClient;

    @BeforeEach
    public void setUp() {
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
    }

    @AfterEach
    public void stop() {
        resetAllScenarios();
    }

    @Test
    void fetchUserTestExponentialBackOff() {
        stubFor(get(urlEqualTo("/users/user")).inScenario("Check retry for user")
            .whenScenarioStateIs("3")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"login\" : \"Emil\", " +
                        "\"updated_at\" : \"2024-02-09T17:47:19Z\"}")
            ));
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.EXPONENTIAL);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        GitHubUserResponse gitHubUserResponse = gitHubClient.fetchUser("user").block();

        assertNotNull(gitHubUserResponse);
        assertEquals(gitHubUserResponse.userName(), "Emil");
        assertEquals(gitHubUserResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchUserTestLinearBackOff() {
        stubFor(get(urlEqualTo("/users/user")).inScenario("Check retry for user")
            .whenScenarioStateIs("3")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"login\" : \"Emil\", " +
                        "\"updated_at\" : \"2024-02-09T17:47:19Z\"}")
            ));
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.LINEAR);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        GitHubUserResponse gitHubUserResponse = gitHubClient.fetchUser("user").block();

        assertNotNull(gitHubUserResponse);
        assertEquals(gitHubUserResponse.userName(), "Emil");
        assertEquals(gitHubUserResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchUserTestConstantBackOff() {
        stubFor(get(urlEqualTo("/users/user")).inScenario("Check retry for user")
            .whenScenarioStateIs("3")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"login\" : \"Emil\", " +
                        "\"updated_at\" : \"2024-02-09T17:47:19Z\"}")
            ));
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.CONSTANT);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        GitHubUserResponse gitHubUserResponse = gitHubClient.fetchUser("user").block();

        assertNotNull(gitHubUserResponse);
        assertEquals(gitHubUserResponse.userName(), "Emil");
        assertEquals(gitHubUserResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void failFetchUserTestExponentialBackOff() {
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
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.EXPONENTIAL);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        StepVerifier.create(gitHubClient.fetchUser("user")).verifyError();
    }

    @Test
    void failFetchUserTestLinearBackOff() {
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
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.LINEAR);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        StepVerifier.create(gitHubClient.fetchUser("user")).verifyError();
    }

    @Test
    void failFetchUserTestConstantBackOff() {
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
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.CONSTANT);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        StepVerifier.create(gitHubClient.fetchUser("user")).verifyError();
    }

    @Test
    void fetchRepositoryTestExponentialBlackOff() {
        stubFor(get(urlEqualTo("/repos/owner/repo")).inScenario("Check retry for repo")
            .whenScenarioStateIs("3")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-type", "application/json")
                    .withBody("{\"name\" : \"repo_name\", " +
                        "\"updated_at\" : \"2024-02-09T17:47:19Z\", " +
                        "\"owner:login\" : \"user\"}")
            ));
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.EXPONENTIAL);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository("owner", "repo").block();

        assertNotNull(repositoryResponse);
        assertEquals(repositoryResponse.repoName(), "repo_name");
        assertEquals(repositoryResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchRepositoryTestLinearBlackOff() {
        stubFor(get(urlEqualTo("/repos/owner/repo")).inScenario("Check retry for repo")
            .whenScenarioStateIs("3")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-type", "application/json")
                    .withBody("{\"name\" : \"repo_name\", " +
                        "\"updated_at\" : \"2024-02-09T17:47:19Z\", " +
                        "\"owner:login\" : \"user\"}")
            ));
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.LINEAR);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository("owner", "repo").block();

        assertNotNull(repositoryResponse);
        assertEquals(repositoryResponse.repoName(), "repo_name");
        assertEquals(repositoryResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void fetchRepositoryTestConstantBlackOff() {
        stubFor(get(urlEqualTo("/repos/owner/repo")).inScenario("Check retry for repo")
            .whenScenarioStateIs("3")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-type", "application/json")
                    .withBody("{\"name\" : \"repo_name\", " +
                        "\"updated_at\" : \"2024-02-09T17:47:19Z\", " +
                        "\"owner:login\" : \"user\"}")
            ));
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.CONSTANT);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository("owner", "repo").block();

        assertNotNull(repositoryResponse);
        assertEquals(repositoryResponse.repoName(), "repo_name");
        assertEquals(repositoryResponse.lastUpdate().toString(), "2024-02-09T17:47:19Z");
    }

    @Test
    void failFetchRepositoryTestExponentialBlackOff() {
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
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.EXPONENTIAL);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        StepVerifier.create(gitHubClient.fetchRepository("owner", "repo")).verifyError();
    }

    @Test
    void failFetchRepositoryTestLinearBlackOff() {
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
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.LINEAR);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        StepVerifier.create(gitHubClient.fetchRepository("owner", "repo")).verifyError();
    }

    @Test
    void failFetchRepositoryTestConstantBlackOff() {
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
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.CONSTANT);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        StepVerifier.create(gitHubClient.fetchRepository("owner", "repo")).verifyError();
    }

    @Test
    void fetchBranchTestExponentialBlackOff() throws IOException {
        String branchesJson = FileUtils.readFileToString(
            new File("src/test/java/edu/java/json/Branches.json"),
            "UTF-8"
        );
        stubFor(get(urlEqualTo("/repos/user/repo/branches")).inScenario("Check retry for branches")
            .whenScenarioStateIs("3")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(branchesJson)
            )
        );
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.EXPONENTIAL);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        ListBranchesResponse branchResponses = gitHubClient.fetchBranch("user", "repo").block();

        assertNotNull(branchResponses);
        assertEquals(branchResponses.listBranches().size(), 2);
        assertEquals(branchResponses.listBranches().getFirst().name(), "hw1");
        assertEquals(branchResponses.listBranches().getLast().name(), "hw2");
    }

    @Test
    void fetchBranchTestLinearBlackOff() throws IOException {
        String branchesJson = FileUtils.readFileToString(
            new File("src/test/java/edu/java/json/Branches.json"),
            "UTF-8"
        );
        stubFor(get(urlEqualTo("/repos/user/repo/branches")).inScenario("Check retry for branches")
            .whenScenarioStateIs("3")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(branchesJson)
            )
        );
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.LINEAR);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        ListBranchesResponse branchResponses = gitHubClient.fetchBranch("user", "repo").block();

        assertNotNull(branchResponses);
        assertEquals(branchResponses.listBranches().size(), 2);
        assertEquals(branchResponses.listBranches().getFirst().name(), "hw1");
        assertEquals(branchResponses.listBranches().getLast().name(), "hw2");
    }

    @Test
    void fetchBranchTestConstantBlackOff() throws IOException {
        String branchesJson = FileUtils.readFileToString(
            new File("src/test/java/edu/java/json/Branches.json"),
            "UTF-8"
        );
        stubFor(get(urlEqualTo("/repos/user/repo/branches")).inScenario("Check retry for branches")
            .whenScenarioStateIs("3")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(branchesJson)
            )
        );
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.LINEAR);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        ListBranchesResponse branchResponses = gitHubClient.fetchBranch("user", "repo").block();

        assertNotNull(branchResponses);
        assertEquals(branchResponses.listBranches().size(), 2);
        assertEquals(branchResponses.listBranches().getFirst().name(), "hw1");
        assertEquals(branchResponses.listBranches().getLast().name(), "hw2");
    }

    @Test
    void failFetchBranchTestExponentialBlackOff() throws IOException {
        String branchesJson = FileUtils.readFileToString(
            new File("src/test/java/edu/java/json/Branches.json"),
            "UTF-8"
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
                ));
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.EXPONENTIAL);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        StepVerifier.create(gitHubClient.fetchBranch("user", "repo")).verifyError();
    }

    @Test
    void failFetchBranchTestLinearBlackOff() throws IOException {
        String branchesJson = FileUtils.readFileToString(
            new File("src/test/java/edu/java/json/Branches.json"),
            "UTF-8"
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
            ));
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.LINEAR);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        StepVerifier.create(gitHubClient.fetchBranch("user", "repo")).verifyError();
    }

    @Test
    void failFetchBranchTestConstantBlackOff() throws IOException {
        String branchesJson = FileUtils.readFileToString(
            new File("src/test/java/edu/java/json/Branches.json"),
            "UTF-8"
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
            ));
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.LINEAR);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Github github = new ClientConfig.Github("", retryPolicy);
        when(clientConfig.github()).thenReturn(github);
        gitHubClient = new GitHubClient(WebClient.create("http://localhost:8001"), clientConfig, retryBuilder);

        StepVerifier.create(gitHubClient.fetchBranch("user", "repo")).verifyError();
    }
}

