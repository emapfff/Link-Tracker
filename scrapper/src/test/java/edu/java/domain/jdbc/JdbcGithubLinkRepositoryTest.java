package edu.java.domain.jdbc;

import edu.java.configuration.DataSourceConfig;
import edu.java.configuration.JdbcTemplateConfig;
import edu.java.configuration.TransactionManagerConfig;
import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {JdbcChatRepository.class, JdbcLinkRepository.class, JdbcConsistsRepository.class, JdbcGithubLinkRepository.class} )
@ContextConfiguration(classes = {JdbcTemplateConfig.class, DataSourceConfig.class, TransactionManagerConfig.class})
@Transactional
@Rollback
class JdbcGithubLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcGithubLinkRepository githubLinkRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;

    private static final URI URI_GITHUB = URI.create("http://github");
    private static final URI URI_STACKOVERFLOW = URI.create("http://stackoverflow");

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
    void findAllByTgChatIdTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        githubLinkRepository.add(1234L, URI_GITHUB, 5);
        githubLinkRepository.add(123L, URI_GITHUB, 10);

        List<GithubLinkDto> githubLinks = githubLinkRepository.findAllByTgChatIdAndUrl(1234L, URI_GITHUB);

        assertEquals(1, githubLinks.size());
        assertEquals(5, githubLinks.getLast().countBranches());
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
