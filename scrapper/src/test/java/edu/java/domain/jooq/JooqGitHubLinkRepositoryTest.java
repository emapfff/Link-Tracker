package edu.java.domain.jooq;

import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "app.database-access-type=jooq")
@Transactional
class JooqGitHubLinkRepositoryTest extends IntegrationTest {
    private static final URI URI_GITHUB = URI.create("http://github");
    private static final URI URI_STACKOVERFLOW = URI.create("http://stackoverflow");
    @Autowired
    private JooqGithubLinkRepository githubLinkRepository;
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private JooqChatRepository chatRepository;

    @Test
    void add() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());

        githubLinkRepository.add(1234L, URI_GITHUB, 5);

        List<GithubLinkDto> githubLinks = githubLinkRepository.findAll();
        LinkDto linkDto = linkRepository.findLinkByChatIdAndUrl(1234L, URI_GITHUB);
        assertEquals(githubLinks.getLast().linkId(), linkDto.id());
        assertEquals(githubLinks.getLast().countBranches(), 5);
    }

    @Test
    void findGithubLinkByLinkIdTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        githubLinkRepository.add(1234L, URI_GITHUB, 5);
        githubLinkRepository.add(123L, URI_GITHUB, 10);
        Long linkId = linkRepository.findLinkByChatIdAndUrl(1234L, URI_GITHUB).id();

        GithubLinkDto githubLinkDto = githubLinkRepository.findGithubLinkByLinkId(linkId);

        assertEquals(githubLinkDto.countBranches(), 5);
        assertEquals(githubLinkDto.linkId(), linkId);
    }

    @Test
    void findAllByTgChatIdAndUrl() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        githubLinkRepository.add(1234L, URI_GITHUB, 5);
        githubLinkRepository.add(123L, URI_GITHUB, 10);

        List<GithubLinkDto> githubLinkDtoList = githubLinkRepository.findAllByTgChatIdAndUrl(123L, URI_GITHUB);

        assertEquals(githubLinkDtoList.size(), 1);
        assertEquals(githubLinkDtoList.getLast().countBranches(), 10);
    }

    @Test
    void checkRemove() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        githubLinkRepository.add(1234L, URI_GITHUB, 5);
        githubLinkRepository.add(123L, URI_GITHUB, 10);

        linkRepository.remove(1234L, URI_GITHUB);

        List<GithubLinkDto> githubLinks = githubLinkRepository.findAllByTgChatIdAndUrl(1234L, URI_GITHUB);
        assertEquals(githubLinks.size(), 0);
    }

    @Test
    void setCountBranchesTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        githubLinkRepository.add(1234L, URI_GITHUB, 5);
        githubLinkRepository.add(123L, URI_GITHUB, 10);
        LinkDto link = linkRepository.findLinkByChatIdAndUrl(123L, URI_GITHUB);

        githubLinkRepository.setCountBranches(link, 5);

        List<GithubLinkDto> githubLinks = githubLinkRepository.findAllByTgChatIdAndUrl(123L, URI_GITHUB);
        assertEquals(githubLinks.size(), 1);
        assertEquals(githubLinks.getLast().countBranches(), 5);

    }
}
