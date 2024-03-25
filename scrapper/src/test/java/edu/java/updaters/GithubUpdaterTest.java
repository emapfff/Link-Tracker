package edu.java.updaters;

import edu.java.clients.GitHubClient;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.responses.RepositoryResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class GithubUpdaterTest {
    private final LinkDto link = new LinkDto();

    @Autowired
    private GithubUpdater githubUpdater;

    @MockBean
    private GitHubClient githubClient;

    @MockBean
    private JdbcLinkRepository linkRepository;

    @BeforeEach
    public void setUp() {
        this.link.setId(1L);
        this.link.setUrl(URI.create("https://github.com/emapfff/java-backend-2024"));
        this.link.setLastUpdate(OffsetDateTime.now());

    }

    @Test
    void updateNoUpdateNeededTest() {
        RepositoryResponse repositoryResponse = new RepositoryResponse("repo", OffsetDateTime.now().minusDays(1));
        when(githubClient.fetchRepository(any(), any())).thenReturn(Mono.just(repositoryResponse));

        int result = githubUpdater.update(link);

        assertEquals(0, result);
    }

    @Test
    void updateUpdateNeededTest() {
        RepositoryResponse repositoryResponse = new RepositoryResponse("repo", OffsetDateTime.now().plusDays(1));
        when(githubClient.fetchRepository(any(), any())).thenReturn(Mono.just(repositoryResponse));
        doNothing().when(linkRepository).setLastUpdate(any(), any());

        int result = githubUpdater.update(link);

        assertEquals(1, result);
    }

}
