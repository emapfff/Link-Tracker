package edu.java.domain.jdbc;

import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JdbcStackOverflowLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcStackOverflowLinkRepository stackOverflowLinkRepository;

    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;

    private static final URI URI_GITHUB = URI.create("http://github");
    private static final URI URI_STACKOVERFLOW = URI.create("http://stackoverflow");
    private static final URI URI_STACKOVERFLOW1 = URI.create("http://stackoverflow1");
    private static final URI URI_STACKOVERFLOW2 = URI.create("http://stackoverflow2");

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());

        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW, 5);

        List<StackOverflowDto> stackOverflowDtos = stackOverflowLinkRepository.findAll();
        LinkDto linkDto = linkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW);
        assertEquals(stackOverflowDtos.getLast().getLinkId(), linkDto.getId());
        assertEquals(stackOverflowDtos.getLast().getAnswerCount(), 5);
    }

    @Test
    @Transactional
    @Rollback
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
        assertEquals(10, stackOverflowDtos.getLast().getAnswerCount());
    }

    @Test
    @Transactional
    @Rollback
    void findStackOverflowLinkIdByLinkIdTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI_GITHUB, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW1, OffsetDateTime.now());
        linkRepository.add(1234L, URI_STACKOVERFLOW2, OffsetDateTime.now());
        linkRepository.add(123L, URI_GITHUB, OffsetDateTime.now());
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW1, 5);
        stackOverflowLinkRepository.add(1234L, URI_STACKOVERFLOW2, 10);

        Long linkId = linkRepository.findLinkByChatIdAndUrl(1234L, URI_STACKOVERFLOW1).getId();

        StackOverflowDto stackOverflowDto = stackOverflowLinkRepository.findStackOverflowLinkByLinkId(linkId);
        assertEquals(stackOverflowDto.getAnswerCount(), 5);
        assertEquals(stackOverflowDto.getLinkId(), linkId);
    }

    @Test
    @Transactional
    @Rollback
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
            URI_STACKOVERFLOW);

        assertEquals(stackOverflowDtos.size(), 0);
    }

    @Test
    @Transactional
    @Rollback
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
        assertEquals(stackOverflowDtoList.getLast().getAnswerCount(), 5);
    }
}
