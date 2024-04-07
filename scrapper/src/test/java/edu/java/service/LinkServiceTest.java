package edu.java.service;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.domain.ChatRepository;
import edu.java.domain.GithubLinkRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.responses.BranchResponse;
import edu.java.responses.QuestionResponse;
import edu.java.responses.RepositoryResponse;
import edu.java.tools.LinkParser;
import edu.java.tools.Resource;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "app.database-access-type=jdbc")
class LinkServiceTest {
    @Mock
    private LinkRepository linkRepository;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private GithubLinkRepository githubLinkRepository;
    @Mock
    private StackOverflowLinkRepository stackOverflowLinkRepository;
    @Mock
    private GitHubClient gitHubClient;
    @Mock
    private StackOverflowClient stackOverflowClient;
    @Mock
    private LinkParser linkParser;
    @InjectMocks
    private LinkService linkService;

    @Test
    void addGithubLinkTest() {
        URI url = URI.create("https://github.com/user/repo");
        Long tgChatId = 123456L;
        RepositoryResponse repositoryResponse = new RepositoryResponse("repo", OffsetDateTime.now());
        List<BranchResponse> branchResponses = List.of(
            new BranchResponse("branch1", new BranchResponse.Commit("sha1", "url1"), false));
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(1);
        when(linkParser.parse(url)).thenReturn(Resource.GITHUB);
        when(gitHubClient.fetchRepository(anyString(), anyString())).thenReturn(Mono.just(repositoryResponse));
        when(gitHubClient.fetchBranch(anyString(), anyString())).thenReturn(Mono.just(branchResponses));
        when(linkParser.getGithubUser(url)).thenReturn("user");
        when(linkParser.getGithubRepo(url)).thenReturn("repo");
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
            new QuestionResponse.ItemResponse(
                false,
                20,
                1642028L,
                OffsetDateTime.now().minusDays(1)
            )
        );
        URI url = URI.create("https://stackoverflow.com/questions/123456");
        Long tgChatId = 123456L;
        QuestionResponse questionResponse = new QuestionResponse(itemsMinusDay);
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(1);
        when(linkParser.parse(url)).thenReturn(Resource.STACKOVERFLOW);
        when(stackOverflowClient.fetchQuestion(anyLong())).thenReturn(Mono.just(questionResponse));
        when(linkParser.getStackOverFlowId(url)).thenReturn(123456L);
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
        Long tgChatId = 1L;
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
        when(linkParser.parse(url)).thenReturn(Resource.INCORRECT_URL);

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

        assertThrows(LinkNotFoundException.class, () -> linkService.remove(tgChatId, url));
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
