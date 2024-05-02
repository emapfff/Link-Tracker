package edu.java.domain.jpa;

import edu.java.domain.ChatRepository;
import edu.java.domain.GithubLinkRepository;
import edu.java.domain.LinkRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(properties = "app.database-access-type=jpa")
@Transactional
class JpaGithubLinkRepositoryTest extends IntegrationTest {
    private static final URI URI_GITHUB = URI.create("http://github");
    private static final URI URI_STACKOVERFLOW = URI.create("http://stackoverflow");
    @Autowired
    private GithubLinkRepository githubLinkRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatRepository chatRepository;

    @Test
    void addTest() {
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
    void findGithubLinkByTgChatIdAndUrlTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        githubLinkRepository.add(1234L, URI_GITHUB, 5);
        githubLinkRepository.add(123L, URI_GITHUB, 10);

        GithubLinkDto githubLink = githubLinkRepository.findGithubLinkByTgChatIdAndUrl(1234L, URI_GITHUB);

        assertNotNull(githubLink);
        assertEquals(5, githubLink.countBranches());
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
    void checkRemove() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        githubLinkRepository.add(1234L, URI_GITHUB, 5);
        githubLinkRepository.add(123L, URI_GITHUB, 10);

        linkRepository.remove(1234L, URI_GITHUB);

        GithubLinkDto githubLinks = githubLinkRepository.findGithubLinkByTgChatIdAndUrl(1234L, URI_GITHUB);
        assertNull(githubLinks);
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

        GithubLinkDto githubLinks = githubLinkRepository.findGithubLinkByTgChatIdAndUrl(123L, URI_GITHUB);
        assertNotNull(githubLinks);
        assertEquals(githubLinks.countBranches(), 5);

    }
}
