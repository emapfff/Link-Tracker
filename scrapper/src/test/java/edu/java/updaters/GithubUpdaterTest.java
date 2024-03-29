package edu.java.updaters;

import edu.java.clients.GitHubClient;
import edu.java.domain.GithubLinkRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.jdbc.JdbcGithubLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.responses.BranchResponse;
import edu.java.responses.RepositoryResponse;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.GithubUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import edu.java.tools.LinkParse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubUpdaterTest extends IntegrationTest{
    private final LinkDto link =
        new LinkDto(1L, URI.create("https://github.com/emapfff/java-backend-2024"), OffsetDateTime.now());

    @Mock
    private GitHubClient githubClient;
    @Mock
    private LinkRepository linkRepository;
    @Mock
    private LinkParse linkParse;
    @Mock
    private GithubLinkRepository githubLinkRepository;

    @InjectMocks
    private GithubUpdater githubUpdater;

    @Test
    void updateNoNeededTest() {
        RepositoryResponse repositoryResponse = new RepositoryResponse("repo", OffsetDateTime.now().minusDays(1));
        when(githubClient.fetchRepository(anyString(), anyString())).thenReturn(Mono.just(repositoryResponse));
        when(linkParse.getGithubUser(link.url())).thenReturn("emapfff");
        when(linkParse.getGithubRepo(link.url())).thenReturn("java-backend-2024");

        boolean result = githubUpdater.update(link);

        assertFalse(result);
    }

    @Test
    void updateNeededTest() {
        RepositoryResponse repositoryResponse = new RepositoryResponse("repo", OffsetDateTime.now().plusDays(1));
        when(githubClient.fetchRepository(any(), any())).thenReturn(Mono.just(repositoryResponse));
        doNothing().when(linkRepository).setLastUpdate(any(), any());
        when(linkParse.getGithubUser(link.url())).thenReturn("emapfff");
        when(linkParse.getGithubRepo(link.url())).thenReturn("java-backend-2024");

        boolean result = githubUpdater.update(link);

        assertTrue(result);
    }

    @Test
    void checkBranchesTrue() {
        GithubLinkDto githubLinkDto = new GithubLinkDto(1L, link.id(), 1);
        when(githubLinkRepository.findGithubLinkByLinkId(link.id())).thenReturn(githubLinkDto);
        List<BranchResponse> branches = List.of(
            new BranchResponse("branch1", new BranchResponse.Commit("sha1", "url1"), false),
            new BranchResponse("branch2", new BranchResponse.Commit("sha2", "url2"), false)
        );
        when(linkParse.getGithubUser(link.url())).thenReturn("emapfff");
        when(linkParse.getGithubRepo(link.url())).thenReturn("java-backend-2024");
        when(githubClient.fetchBranch(any(), any())).thenReturn(Mono.just(branches));
        doNothing().when(githubLinkRepository).setCountBranches(any(), any());

        boolean result = githubUpdater.checkBranches(link);

        assertTrue(result);
    }

    @Test
    void checkBranchesFalse() {
        GithubLinkDto githubLinkDto = new GithubLinkDto(1L, link.id(), 2);
        when(githubLinkRepository.findGithubLinkByLinkId(link.id())).thenReturn(githubLinkDto);
        List<BranchResponse> branches = List.of(
            new BranchResponse("branch1", new BranchResponse.Commit("sha1", "url1"), false),
            new BranchResponse("branch2", new BranchResponse.Commit("sha2", "url2"), false)
        );
        when(linkParse.getGithubUser(link.url())).thenReturn("emapfff");
        when(linkParse.getGithubRepo(link.url())).thenReturn("java-backend-2024");
        when(githubClient.fetchBranch(any(), any())).thenReturn(Mono.just(branches));

        boolean result = githubUpdater.checkBranches(link);

        assertFalse(result);
    }



}
