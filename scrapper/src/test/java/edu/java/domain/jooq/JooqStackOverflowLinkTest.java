package edu.java.domain.jooq;

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

@SpringBootTest(properties = "app.database-access-type=jooq")
@Transactional
public class JooqStackOverflowLinkTest extends IntegrationTest {
    private static final URI URI_GITHUB = URI.create("http://github");
    private static final URI URI_STACKOVERFLOW = URI.create("http://stackoverflow");
    private static final URI URI_STACKOVERFLOW1 = URI.create("http://stackoverflow1");
    private static final URI URI_STACKOVERFLOW2 = URI.create("http://stackoverflow2");
    @Autowired
    private JooqStackOverflowLinkRepository stackOverflowLinkRepository;
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private JooqChatRepository chatRepository;

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
    void findAllByTgChatIdTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);

        List<StackOverflowDto> stackOverflowDtos = stackOverflowLinkRepository.findAllByTgChatIdAndUrl(
            1234L,
            URI_STACKOVERFLOW2
        );

        assertEquals(1, stackOverflowDtos.size());
        assertEquals(10, stackOverflowDtos.getLast().answerCount());
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

        List<StackOverflowDto> stackOverflowDtos = stackOverflowLinkRepository.findAllByTgChatIdAndUrl(
            1234L,
            URI_STACKOVERFLOW
        );

        assertEquals(stackOverflowDtos.size(), 0);
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

        List<StackOverflowDto> stackOverflowDtoList = stackOverflowLinkRepository
            .findAllByTgChatIdAndUrl(1234L, URI_STACKOVERFLOW2);
        assertEquals(stackOverflowDtoList.size(), 1);
        assertEquals(stackOverflowDtoList.getLast().answerCount(), 5);
    }
}