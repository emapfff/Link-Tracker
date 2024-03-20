package edu.java.scheduler;

import edu.java.clients.GitHubClient;
import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import edu.java.responses.RepositoryResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class GithubUpdaterTest {
    private final LinkDto link = new LinkDto();
    @Autowired
    private GithubUpdater githubUpdater;
    @MockBean
    private GitHubClient githubClient;

    @BeforeEach
    public void setUp() {
        this.link.setId(1);
        this.link.setUrl(URI.create("https://github.com/emapfff/java-backend-2024"));
        this.link.setLastUpdate(OffsetDateTime.now());

    }

    @Test
    void updateTestReturn0() {
        when(githubClient.fetchRepository("emapfff", "java-backend-2024"))
            .thenReturn(Mono.just(new RepositoryResponse(
                "java-backend-2024",
                OffsetDateTime.parse("2024-02-09T17:47:19Z")
            )));

        int result = githubUpdater.update(link);

        assertEquals(result, 0);
    }

    @Test
    void updateTestReturn1() {
        when(githubClient.fetchRepository("emapfff", "java-backend-2024"))
            .thenReturn(Mono.just(new RepositoryResponse(
                "java-backend-2024",
                OffsetDateTime.parse("2025-02-09T17:47:19Z")
            )));

        int result = githubUpdater.update(link);

        assertEquals(result, 1);
    }
}
