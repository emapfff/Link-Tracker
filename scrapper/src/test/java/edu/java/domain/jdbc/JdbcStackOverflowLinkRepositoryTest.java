package edu.java.domain.jdbc;

import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
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

@SpringBootTest(properties = "app.database-access-type=jdbc")
@Transactional
class JdbcStackOverflowLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcStackOverflowLinkRepository jdbcStackOverflowLinkRepository;

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    private static final URI URI_GITHUB = URI.create("http://github");
    private static final URI URI_STACKOVERFLOW = URI.create("http://stackoverflow");
    private static final URI URI_STACKOVERFLOW1 = URI.create("http://stackoverflow1");
    private static final URI URI_STACKOVERFLOW2 = URI.create("http://stackoverflow2");

    @Test
    void addTest() {
        jdbcChatRepository.add(1234L);
        jdbcChatRepository.add(123L);
        jdbcLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jdbcLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());

        jdbcStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW, 5);

        List<StackOverflowDto> stackOverflowDtos = jdbcStackOverflowLinkRepository.findAll();
        LinkDto linkDto = jdbcLinkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW);
        assertEquals(stackOverflowDtos.getLast().linkId(), linkDto.id());
        assertEquals(stackOverflowDtos.getLast().answerCount(), 5);
    }

    @Test
    void findStackOverflowLinkByTgChatIdAndUrlTest() {
        jdbcChatRepository.add(1234L);
        jdbcChatRepository.add(123L);
        jdbcLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        jdbcLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jdbcStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        jdbcStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);

        StackOverflowDto stackOverflowLink = jdbcStackOverflowLinkRepository.findStackOverflowLinkByTgChatIdAndUrl(
            1234L,
            URI_STACKOVERFLOW2
        );

        assertNotNull(stackOverflowLink);
        assertEquals(10, stackOverflowLink.answerCount());
    }

    @Test
    void findStackOverflowLinkIdByLinkIdTest() {
        jdbcChatRepository.add(1234L);
        jdbcChatRepository.add(123L);
        jdbcLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        jdbcLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jdbcStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        jdbcStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);

        Long linkId = jdbcLinkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW1).id();

        StackOverflowDto stackOverflowDto = jdbcStackOverflowLinkRepository.findStackOverflowLinkByLinkId(linkId);
        assertEquals(stackOverflowDto.answerCount(), 5);
        assertEquals(stackOverflowDto.linkId(), linkId);
    }

    @Test
    void checkRemove() {
        jdbcChatRepository.add(1234L);
        jdbcChatRepository.add(123L);
        jdbcLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jdbcLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jdbcStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW, 5);

        jdbcLinkRepository.remove(1234L, URI_STACKOVERFLOW);

        StackOverflowDto stackOverflowLink = jdbcStackOverflowLinkRepository.findStackOverflowLinkByTgChatIdAndUrl(
            1234L,
            URI_STACKOVERFLOW);

        assertNull(stackOverflowLink);
    }

    @Test
    void setAnswersCountTest() {
        jdbcChatRepository.add(1234L);
        jdbcChatRepository.add(123L);
        jdbcLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        jdbcLinkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        jdbcLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jdbcStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        jdbcStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);
        LinkDto link = jdbcLinkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW2);

        jdbcStackOverflowLinkRepository.setAnswersCount(link, 5);

        StackOverflowDto stackOverflowLink = jdbcStackOverflowLinkRepository
            .findStackOverflowLinkByTgChatIdAndUrl(1234L, URI_STACKOVERFLOW2);
        assertNotNull(stackOverflowLink);
        assertEquals(stackOverflowLink.answerCount(), 5);
    }
}
