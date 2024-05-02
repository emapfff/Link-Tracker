package edu.java.domain.jdbc;

import edu.java.domain.ChatRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.StackOverflowLinkRepository;
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
    private static final URI URI_GITHUB = URI.create("http://github");
    private static final URI URI_STACKOVERFLOW = URI.create("http://stackoverflow");
    private static final URI URI_STACKOVERFLOW1 = URI.create("http://stackoverflow1");
    private static final URI URI_STACKOVERFLOW2 = URI.create("http://stackoverflow2");
    @Autowired
    private StackOverflowLinkRepository stackOverflowLinkRepository;
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

        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW, 5);

        List<StackOverflowDto> stackOverflowDtos = stackOverflowLinkRepository.findAll();
        LinkDto linkDto = linkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW);
        assertEquals(stackOverflowDtos.getLast().linkId(), linkDto.id());
        assertEquals(stackOverflowDtos.getLast().answerCount(), 5);
    }

    @Test
    void findStackOverflowLinkByTgChatIdAndUrlTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);

        StackOverflowDto stackOverflowLink = stackOverflowLinkRepository.findStackOverflowLinkByTgChatIdAndUrl(
            1234L,
            URI_STACKOVERFLOW2
        );

        assertNotNull(stackOverflowLink);
        assertEquals(10, stackOverflowLink.answerCount());
    }

    @Test
    void findStackOverflowLinkIdByLinkIdTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);

        Long linkId = linkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW1).id();

        StackOverflowDto stackOverflowDto = stackOverflowLinkRepository.findStackOverflowLinkByLinkId(linkId);
        assertEquals(stackOverflowDto.answerCount(), 5);
        assertEquals(stackOverflowDto.linkId(), linkId);
    }

    @Test
    void checkRemove() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW, 5);

        linkRepository.remove(1234L, URI_STACKOVERFLOW);

        StackOverflowDto stackOverflowLink = stackOverflowLinkRepository.findStackOverflowLinkByTgChatIdAndUrl(
            1234L,
            URI_STACKOVERFLOW
        );

        assertNull(stackOverflowLink);
    }

    @Test
    void setAnswersCountTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);
        LinkDto link = linkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW2);

        stackOverflowLinkRepository.setAnswersCount(link, 5);

        StackOverflowDto stackOverflowLink = stackOverflowLinkRepository
            .findStackOverflowLinkByTgChatIdAndUrl(1234L, URI_STACKOVERFLOW2);
        assertNotNull(stackOverflowLink);
        assertEquals(stackOverflowLink.answerCount(), 5);
    }
}
