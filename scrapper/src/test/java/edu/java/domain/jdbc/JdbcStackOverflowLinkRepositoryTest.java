package edu.java.domain.jdbc;

import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
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
class JdbcStackOverflowLinkRepositoryTest {
    @Autowired
    private JdbcStackOverflowLinkRepository stackOverflowLinkRepository;

    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI.create("http://github"), OffsetDateTime.now());
        linkRepository.add(1234L, URI.create("http://stackoverflow"), OffsetDateTime.now());
        linkRepository.add(123L, URI.create("http://github"), OffsetDateTime.now());

        stackOverflowLinkRepository.add(1234L, URI.create("http://stackoverflow"), 5);

        List<StackOverflowDto> stackOverflowDtos = stackOverflowLinkRepository.findAll();
        LinkDto linkDto = linkRepository.findLinkByChatIdAndUrl(1234L, URI.create("http://stackoverflow"));
        assertEquals(stackOverflowDtos.getLast().getLinkId(), linkDto.getId());
        assertEquals(stackOverflowDtos.getLast().getAnswerCount(), 5);
    }

    @Test
    @Transactional
    @Rollback
    void findAllByTgChatIdTest() {
        chatRepository.add(1234L);
        chatRepository.add(123L);
        linkRepository.add(1234L, URI.create("http://github"), OffsetDateTime.now());
        linkRepository.add(1234L, URI.create("http://stackoverflow1"), OffsetDateTime.now());
        linkRepository.add(1234L, URI.create("http://stackoverflow2"), OffsetDateTime.now());
        linkRepository.add(123L, URI.create("http://github"), OffsetDateTime.now());
        stackOverflowLinkRepository.add(1234L, URI.create("http://stackoverflow1"), 5);
        stackOverflowLinkRepository.add(1234L, URI.create("http://stackoverflow2"), 10);

        List<StackOverflowDto> stackOverflowDtos = stackOverflowLinkRepository.findAllByTgChatIdAndUrl(
            1234L,
            URI.create("http://stackoverflow2")
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
        linkRepository.add(1234L, URI.create("http://github"), OffsetDateTime.now());
        linkRepository.add(1234L, URI.create("http://stackoverflow1"), OffsetDateTime.now());
        linkRepository.add(1234L, URI.create("http://stackoverflow2"), OffsetDateTime.now());
        linkRepository.add(123L, URI.create("http://github"), OffsetDateTime.now());
        stackOverflowLinkRepository.add(1234L, URI.create("http://stackoverflow1"), 5);
        stackOverflowLinkRepository.add(1234L, URI.create("http://stackoverflow2"), 10);

        Long linkId = linkRepository.findLinkByChatIdAndUrl(1234L, URI.create("http://stackoverflow1")).getId();

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
        linkRepository.add(1234L, URI.create("http://github"), OffsetDateTime.now());
        linkRepository.add(1234L, URI.create("http://stackoverflow"), OffsetDateTime.now());
        linkRepository.add(123L, URI.create("http://github"), OffsetDateTime.now());
        stackOverflowLinkRepository.add(1234L, URI.create("http://stackoverflow"), 5);

        linkRepository.remove(1234L, URI.create("http://stackoverflow"));

        List<StackOverflowDto> stackOverflowDtos = stackOverflowLinkRepository.findAllByTgChatIdAndUrl(
            1234L,
            URI.create("http://stackoverflow"));

        assertEquals(stackOverflowDtos.size(), 0);
    }
}
