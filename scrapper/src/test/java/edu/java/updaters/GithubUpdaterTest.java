package edu.java.updaters;

import edu.java.clients.GitHubClient;
import edu.java.configuration.DataSourceConfig;
import edu.java.configuration.JdbcTemplateConfig;
import edu.java.configuration.TransactionManagerConfig;
import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.jdbc.JdbcConsistsRepository;
import edu.java.domain.jdbc.JdbcGithubLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.responses.BranchResponse;
import edu.java.responses.RepositoryResponse;
import edu.java.scrapper.IntegrationTest;
import edu.java.tools.LinkParse;
import io.restassured.module.spring.commons.config.ClientConfig;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {JdbcLinkRepository.class, JdbcConsistsRepository.class, JdbcGithubLinkRepository.class,
    GitHubClient.class, GithubUpdater.class, LinkParse.class, JdbcGithubLinkRepository.class})
@ContextConfiguration(classes = {JdbcTemplateConfig.class, DataSourceConfig.class, TransactionManagerConfig.class,
    ClientConfig.class})
@Transactional
@Rollback
class GithubUpdaterTest extends IntegrationTest {
    private final LinkDto link =
        new LinkDto(1L, URI.create("https://github.com/emapfff/java-backend-2024"), OffsetDateTime.now());

    @Autowired
    private GithubUpdater githubUpdater;

    @MockBean
    private GitHubClient githubClient;

    @MockBean
    private JdbcLinkRepository linkRepository;

    @MockBean
    private JdbcGithubLinkRepository githubLinkRepository;

    @Test
    void updateNoNeededTest() {
        RepositoryResponse repositoryResponse = new RepositoryResponse("repo", OffsetDateTime.now().minusDays(1));
        when(githubClient.fetchRepository(any(), any())).thenReturn(Mono.just(repositoryResponse));

        boolean result = githubUpdater.update(link);

        assertFalse(result);
    }

    @Test
    void updateNeededTest() {
        RepositoryResponse repositoryResponse = new RepositoryResponse("repo", OffsetDateTime.now().plusDays(1));
        when(githubClient.fetchRepository(any(), any())).thenReturn(Mono.just(repositoryResponse));
        doNothing().when(linkRepository).setLastUpdate(any(), any());

        boolean result = githubUpdater.update(link);

        assertTrue(result);
    }

    @Test
    void checkBranchesTrue() {
        GithubLinkDto githubLinkDto = new GithubLinkDto(1L, link.id(), 1);
        when(githubLinkRepository.findGithubLinkByLinkId(link.id())).thenReturn(githubLinkDto);
        List<BranchResponse> branches = List.of(new BranchResponse("branch1", new BranchResponse.Commit("sha1", "url1"), false),
            new BranchResponse("branch2", new BranchResponse.Commit("sha2", "url2"), false));
        when(githubClient.fetchBranch(any(), any())).thenReturn(Mono.just(branches));
        doNothing().when(githubLinkRepository).setCountBranches(any(), any());

        boolean result = githubUpdater.checkBranches(link);

        assertTrue(result);
    }

    @Test
    void checkBranchesFalse() {
        GithubLinkDto githubLinkDto = new GithubLinkDto(1L, link.id(), 2);
        when(githubLinkRepository.findGithubLinkByLinkId(link.id())).thenReturn(githubLinkDto);
        List<BranchResponse> branches = List.of(new BranchResponse("branch1", new BranchResponse.Commit("sha1", "url1"), false),
            new BranchResponse("branch2", new BranchResponse.Commit("sha2", "url2"), false));
        when(githubClient.fetchBranch(any(), any())).thenReturn(Mono.just(branches));
        doNothing().when(githubLinkRepository).setCountBranches(any(), any());

        boolean result = githubUpdater.checkBranches(link);

        assertFalse(result);
    }



}
