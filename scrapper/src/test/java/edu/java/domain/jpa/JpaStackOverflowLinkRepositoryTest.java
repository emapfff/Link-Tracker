package edu.java.domain.jpa;

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

@SpringBootTest(properties = "app.database-access-type=jpa")
@Transactional
class JpaStackOverflowLinkRepositoryTest extends IntegrationTest {
    private static final URI URI_GITHUB = URI.create("http://github");
    private static final URI URI_STACKOVERFLOW = URI.create("http://stackoverflow");
    private static final URI URI_STACKOVERFLOW1 = URI.create("http://stackoverflow1");
    private static final URI URI_STACKOVERFLOW2 = URI.create("http://stackoverflow2");
    @Autowired
    private JpaStackOverflowLinkRepository jpaStackOverflowLinkRepository;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;
    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Test
    void addTest() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(123L);
        jpaLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jpaLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());

        jpaStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW, 5);

        List<StackOverflowDto> stackOverflowDtos = jpaStackOverflowLinkRepository.findAll();
        LinkDto linkDto = jpaLinkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW);
        assertEquals(stackOverflowDtos.getLast().linkId(), linkDto.id());
        assertEquals(stackOverflowDtos.getLast().answerCount(), 5);
    }

    @Test
    void findStackOverflowLinkByTgChatIdAndUrlTest() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(123L);
        jpaLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        jpaLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jpaStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        jpaStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);

        StackOverflowDto stackOverflowLink = jpaStackOverflowLinkRepository.findStackOverflowLinkByTgChatIdAndUrl(
            1234L,
            URI_STACKOVERFLOW2
        );

        assertNotNull(stackOverflowLink);
        assertEquals(10, stackOverflowLink.answerCount());
    }

    @Test
    void findStackOverflowLinkIdByLinkIdTest() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(123L);
        jpaLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        jpaLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jpaStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        jpaStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);

        Long linkId = jpaLinkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW1).id();

        StackOverflowDto stackOverflowDto = jpaStackOverflowLinkRepository.findStackOverflowLinkByLinkId(linkId);
        assertEquals(stackOverflowDto.answerCount(), 5);
        assertEquals(stackOverflowDto.linkId(), linkId);
    }

    @Test
    void checkRemove() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(123L);
        jpaLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        jpaLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jpaStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW, 5);

        jpaLinkRepository.remove(1234L, URI_STACKOVERFLOW);

        StackOverflowDto stackOverflowLink = jpaStackOverflowLinkRepository.findStackOverflowLinkByTgChatIdAndUrl(
            1234L,
            URI_STACKOVERFLOW
        );

        assertNull(stackOverflowLink);
    }

    @Test
    void setAnswersCountTest() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(123L);
        jpaLinkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        jpaLinkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        jpaLinkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        jpaStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        jpaStackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);
        LinkDto link = jpaLinkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW2);

        jpaStackOverflowLinkRepository.setAnswersCount(link, 5);

        StackOverflowDto stackOverflowLink = jpaStackOverflowLinkRepository
            .findStackOverflowLinkByTgChatIdAndUrl(1234L, URI_STACKOVERFLOW2);
        assertNotNull(stackOverflowLink);
        assertEquals(stackOverflowLink.answerCount(), 5);
    }
}
