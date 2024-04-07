package edu.java.domain.jdbc;

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

@SpringBootTest(properties = "app.database-access-type=jdbc")
@Transactional
class JdbcGithubLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcGithubLinkRepository jdbcGithubLinkRepository;
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    private static final URI URI_GITHUB = URI.create("http://github");
    private static final URI URI_STACKOVERFLOW = URI.create("http://stackoverflow");

    @Test
    void addTest() {
        jdbcChatRepository.add(1234L);
        jdbcChatRepository.add(123L);
        jdbcLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jdbcLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());

        jdbcGithubLinkRepository.add(1234L, URI_GITHUB, 5);

        List<GithubLinkDto> githubLinks = jdbcGithubLinkRepository.findAll();
        LinkDto linkDto = jdbcLinkRepository.findLinkByChatIdAndUrl(1234L, URI_GITHUB);
        assertEquals(githubLinks.getLast().linkId(), linkDto.id());
        assertEquals(githubLinks.getLast().countBranches(), 5);
    }

    @Test
    void findAllByTgChatIdTest() {
        jdbcChatRepository.add(1234L);
        jdbcChatRepository.add(123L);
        jdbcLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jdbcLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jdbcGithubLinkRepository.add(1234L, URI_GITHUB, 5);
        jdbcGithubLinkRepository.add(123L, URI_GITHUB, 10);

        List<GithubLinkDto> githubLinks = jdbcGithubLinkRepository.findAllByTgChatIdAndUrl(1234L, URI_GITHUB);

        assertEquals(1, githubLinks.size());
        assertEquals(5, githubLinks.getLast().countBranches());
    }

    @Test
    void findGithubLinkByLinkIdTest() {
        jdbcChatRepository.add(1234L);
        jdbcChatRepository.add(123L);
        jdbcLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jdbcLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jdbcGithubLinkRepository.add(1234L, URI_GITHUB, 5);
        jdbcGithubLinkRepository.add(123L, URI_GITHUB, 10);
        Long linkId = jdbcLinkRepository.findLinkByChatIdAndUrl(1234L, URI_GITHUB).id();

        GithubLinkDto githubLinkDto = jdbcGithubLinkRepository.findGithubLinkByLinkId(linkId);

        assertEquals(githubLinkDto.countBranches(), 5);
        assertEquals(githubLinkDto.linkId(), linkId);
    }
    @Test
    void checkRemove() {
        jdbcChatRepository.add(1234L);
        jdbcChatRepository.add(123L);
        jdbcLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jdbcLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jdbcGithubLinkRepository.add(1234L, URI_GITHUB, 5);
        jdbcGithubLinkRepository.add(123L, URI_GITHUB, 10);

        jdbcLinkRepository.remove(1234L, URI_GITHUB);

        List<GithubLinkDto> githubLinks = jdbcGithubLinkRepository.findAllByTgChatIdAndUrl(1234L, URI_GITHUB);
        assertEquals(githubLinks.size(), 0);
    }

    @Test
    void setCountBranchesTest() {
        jdbcChatRepository.add(1234L);
        jdbcChatRepository.add(123L);
        jdbcLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jdbcLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jdbcGithubLinkRepository.add(1234L, URI_GITHUB, 5);
        jdbcGithubLinkRepository.add(123L, URI_GITHUB, 10);
        LinkDto link = jdbcLinkRepository.findLinkByChatIdAndUrl(123L, URI_GITHUB);

        jdbcGithubLinkRepository.setCountBranches(link, 5);

        List<GithubLinkDto> githubLinks = jdbcGithubLinkRepository.findAllByTgChatIdAndUrl(123L, URI_GITHUB);
        assertEquals(githubLinks.size(), 1);
        assertEquals(githubLinks.getLast().countBranches(), 5);

    }
}
