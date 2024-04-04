package edu.java.domain.jpa;

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
    @Autowired
    private JpaGithubLinkRepository jpaGithubLinkRepository;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;
    @Autowired
    private JpaChatRepository jpaChatRepository;

    private static final URI URI_GITHUB = URI.create("http://github");
    private static final URI URI_STACKOVERFLOW = URI.create("http://stackoverflow");

    @Test
    void add() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(123L);
        jpaLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jpaLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());

        jpaGithubLinkRepository.add(1234L, URI_GITHUB, 5);

        List<GithubLinkDto> githubLinks = jpaGithubLinkRepository.findAll();
        LinkDto linkDto = jpaLinkRepository.findLinkByChatIdAndUrl(1234L, URI_GITHUB);
        assertEquals(githubLinks.getLast().linkId(), linkDto.id());
        assertEquals(githubLinks.getLast().countBranches(), 5);
    }

    @Test
    void findGithubLinkByTgChatIdAndUrlTest() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(123L);
        jpaLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jpaLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jpaGithubLinkRepository.add(1234L, URI_GITHUB, 5);
        jpaGithubLinkRepository.add(123L, URI_GITHUB, 10);

        GithubLinkDto githubLink = jpaGithubLinkRepository.findGithubLinkByTgChatIdAndUrl(1234L, URI_GITHUB);

        assertNotNull(githubLink);
        assertEquals(5, githubLink.countBranches());
    }
    @Test
    void findGithubLinkByLinkIdTest() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(123L);
        jpaLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jpaLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jpaGithubLinkRepository.add(1234L, URI_GITHUB, 5);
        jpaGithubLinkRepository.add(123L, URI_GITHUB, 10);
        Long linkId = jpaLinkRepository.findLinkByChatIdAndUrl(1234L, URI_GITHUB).id();

        GithubLinkDto githubLinkDto = jpaGithubLinkRepository.findGithubLinkByLinkId(linkId);

        assertEquals(githubLinkDto.countBranches(), 5);
        assertEquals(githubLinkDto.linkId(), linkId);
    }

    @Test
    void checkRemove() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(123L);
        jpaLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jpaLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jpaGithubLinkRepository.add(1234L, URI_GITHUB, 5);
        jpaGithubLinkRepository.add(123L, URI_GITHUB, 10);

        jpaLinkRepository.remove(1234L, URI_GITHUB);

        GithubLinkDto githubLinks = jpaGithubLinkRepository.findGithubLinkByTgChatIdAndUrl(1234L, URI_GITHUB);
        assertNull(githubLinks);
    }
    @Test
    void setCountBranchesTest() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(123L);
        jpaLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jpaLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jpaGithubLinkRepository.add(1234L, URI_GITHUB, 5);
        jpaGithubLinkRepository.add(123L, URI_GITHUB, 10);
        LinkDto link = jpaLinkRepository.findLinkByChatIdAndUrl(123L, URI_GITHUB);

        jpaGithubLinkRepository.setCountBranches(link, 5);

        GithubLinkDto githubLinks = jpaGithubLinkRepository.findGithubLinkByTgChatIdAndUrl(123L, URI_GITHUB);
        assertNotNull(githubLinks);
        assertEquals(githubLinks.countBranches(), 5);

    }
}
