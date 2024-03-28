package edu.java.service.jdbc;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.domain.jdbc.JdbcGithubLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcStackOverflowLinkRepository;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.responses.BranchResponse;
import edu.java.responses.QuestionResponse;
import edu.java.responses.RepositoryResponse;
import edu.java.tools.LinkParse;
import edu.java.tools.Urls;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JdbcLinkServiceTest {

    private JdbcLinkRepository linkRepository;
    private JdbcChatRepository chatRepository;
    private JdbcGithubLinkRepository githubLinkRepository;
    private JdbcStackOverflowLinkRepository stackOverflowLinkRepository;
    private GitHubClient gitHubClient;
    private StackOverflowClient stackOverflowClient;
    private LinkParse linkParse;
    private JdbcLinkService linkService;

    @BeforeEach
    void setUp() {
        linkRepository = mock(JdbcLinkRepository.class);
        chatRepository = mock(JdbcChatRepository.class);
        githubLinkRepository = mock(JdbcGithubLinkRepository.class);
        stackOverflowLinkRepository = mock(JdbcStackOverflowLinkRepository.class);
        gitHubClient = mock(GitHubClient.class);
        stackOverflowClient = mock(StackOverflowClient.class);
        linkParse = mock(LinkParse.class);
        linkService = new JdbcLinkService(
            linkRepository,
            chatRepository,
            linkParse,
            gitHubClient,
            stackOverflowClient,
            githubLinkRepository,
            stackOverflowLinkRepository
        );
    }

    @Test
    void addGithubLinkTest() {
        URI url = URI.create("https://github.com/user/repo");
        Long tgChatId = 123456L;
        RepositoryResponse repositoryResponse = new RepositoryResponse("repo", OffsetDateTime.now());
        List<BranchResponse> branchResponses = List.of(
            new BranchResponse("branch1", new BranchResponse.Commit("sha1", "url1"), false));
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(1);
        when(linkParse.parse(url)).thenReturn(Urls.GITHUB);
        when(gitHubClient.fetchRepository(anyString(), anyString())).thenReturn(Mono.just(repositoryResponse));
        when(gitHubClient.fetchBranch(anyString(), anyString())).thenReturn(Mono.just(branchResponses));
        when(linkParse.getGithubUser(url)).thenReturn("user");
        when(linkParse.getGithubRepo(url)).thenReturn("repo");
        when(linkRepository.findAllByUrl(url)).thenReturn(List.of(new LinkDto(1L, url, OffsetDateTime.now())));

        LinkDto addedLink = linkService.add(tgChatId, url);

        assertNotNull(addedLink);
        assertEquals(url, addedLink.url());
        verify(linkRepository, times(1)).add(eq(tgChatId), eq(url), any(OffsetDateTime.class));
        verify(githubLinkRepository, times(1)).add(eq(tgChatId), eq(url), anyInt());
    }

    @Test
    void addStackOverflowLinkTest() {
        List<QuestionResponse.ItemResponse> itemsMinusDay = List.of(
            new QuestionResponse.ItemResponse(false, 20, 1642028L, OffsetDateTime.now().minusDays(1)));

        URI url = URI.create("https://stackoverflow.com/questions/123456");
        Long tgChatId = 123456L;
        QuestionResponse questionResponse = new QuestionResponse(itemsMinusDay);
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(1);
        when(linkParse.parse(url)).thenReturn(Urls.STACKOVERFLOW);
        when(stackOverflowClient.fetchQuestion(anyLong())).thenReturn(Mono.just(questionResponse));
        when(linkParse.getStackOverFlowId(url)).thenReturn(123456L);
        when(linkRepository.findAllByUrl(url)).thenReturn(List.of(new LinkDto(1L, url, OffsetDateTime.now())));
        doNothing().when(linkRepository).add(anyLong(), any(), any());

        LinkDto addedLink = linkService.add(tgChatId, url);

        assertNotNull(addedLink);
        assertEquals(url, addedLink.url());
        verify(linkRepository, times(1)).add(eq(tgChatId), eq(url), any(OffsetDateTime.class));
        verify(stackOverflowLinkRepository, times(1)).add(eq(tgChatId), eq(url), anyInt());
    }

    @Test
    void addInvalidChatIdTest() {
        URI url = URI.create("https://github.com/example/repo");
        Long tgChatId = -1L;
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(0);

        assertThrows(IncorrectParametersException.class, () -> linkService.add(tgChatId, url));
        verifyNoInteractions(linkRepository);
        verifyNoInteractions(githubLinkRepository);
    }

    @Test
    void addInvalidUrlTest() {
        URI url = URI.create("https://invalid.url");
        Long tgChatId = 123456L;
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(1);
        when(linkParse.parse(url)).thenReturn(Urls.INCORRECT_URL);

        assertThrows(IncorrectParametersException.class, () -> linkService.add(tgChatId, url));
        verifyNoInteractions(linkRepository);
        verifyNoInteractions(githubLinkRepository);
    }

    @Test
    void removeExistingLinkTest() {
        URI url = URI.create("https://github.com/example/repo");
        Long tgChatId = 123456L;
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(1);
        when(linkRepository.existLinkByUriAndTgChatId(tgChatId, url)).thenReturn(1);
        when(linkRepository.findLinkByChatIdAndUrl(tgChatId, url)).thenReturn(new LinkDto(
            1L,
            URI.create("https://github.com/example/repo"),
            OffsetDateTime.now()
        ));

        LinkDto removedLink = linkService.remove(tgChatId, url);

        assertNotNull(removedLink);
        verify(linkRepository, times(1)).remove(tgChatId, url);
    }

    @Test
    void removeNonExistingLinkTest() {
        URI url = URI.create("https://github.com/example/repo");
        Long tgChatId = 123456L;
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(1);
        when(linkRepository.existLinkByUriAndTgChatId(tgChatId, url)).thenReturn(0);

        assertThrows(IncorrectParametersException.class, () -> linkService.remove(tgChatId, url));
    }

    @Test
    void listAllExistingChatTest() {
        Long tgChatId = 123456L;
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(1);
        when(linkRepository.findAllByTgChatId(tgChatId)).thenReturn(List.of(new LinkDto(
            1L,
            URI.create("https://github.com/example/repo"),
            OffsetDateTime.now()
        )));

        Collection<LinkDto> links = linkService.listAll(tgChatId);

        assertNotNull(links);
        assertEquals(1, links.size());
    }

    @Test
    void listAllNonExistingChatTest() {
        Long tgChatId = 123456L;
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(0);

        assertThrows(IncorrectParametersException.class, () -> linkService.listAll(tgChatId));
        verifyNoInteractions(linkRepository);
    }
}
