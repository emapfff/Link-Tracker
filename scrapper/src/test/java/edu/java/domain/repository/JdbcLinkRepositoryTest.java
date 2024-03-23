package edu.java.domain.repository;

import edu.java.domain.dto.LinkDto;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JdbcLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;
    private static LinkDto firstTuple;
    private static LinkDto secondTuple;

    @BeforeAll
    public static void setUp() {
        firstTuple = new LinkDto();
        firstTuple.setId(1L);
        firstTuple.setUrl(URI.create("http://mycore1"));
        firstTuple.setLastUpdate(OffsetDateTime.now());
        secondTuple = new LinkDto();
        secondTuple.setId(2L);
        secondTuple.setUrl(URI.create("http://mycore2"));
        secondTuple.setLastUpdate(OffsetDateTime.now());
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, firstTuple.getUrl(), firstTuple.getLastUpdate());
        linkRepository.add(22L, secondTuple.getUrl(), secondTuple.getLastUpdate());

        List<LinkDto> listOfChats = linkRepository.findAll();
        assertEquals(listOfChats.getFirst().getUrl(), firstTuple.getUrl());
        assertEquals(listOfChats.getLast().getUrl(), secondTuple.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, firstTuple.getUrl(), firstTuple.getLastUpdate());
        linkRepository.add(22L, secondTuple.getUrl(), secondTuple.getLastUpdate());
        URI link = URI.create("http://mycore2");

        linkRepository.remove(22L, link);

        List<LinkDto> chatDtoList = linkRepository.findAll();
        assertEquals(chatDtoList.size(), 1);
        assertEquals(chatDtoList.getFirst().getUrl(), URI.create("http://mycore1"));
    }

    @Test
    @Transactional
    @Rollback
    void findLinkIdByChatIdAndUrlTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, firstTuple.getUrl(), firstTuple.getLastUpdate());
        linkRepository.add(22L, secondTuple.getUrl(), secondTuple.getLastUpdate());

        Long linkId = linkRepository.findLinkIdByChatIdAndUrl(11L, URI.create("http://mycore1")).getId();

        assertEquals(linkId, 1);
    }

    @Test
    @Transactional
    @Rollback
    void findAllByTgChatIdTest(){
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, URI.create("http://mycore1"), OffsetDateTime.now());
        linkRepository.add(11L, URI.create("http://mycore2"), OffsetDateTime.now());
        linkRepository.add(11L, URI.create("http://mycore3"), OffsetDateTime.now());

        List<LinkDto> linkDtos = linkRepository.findAllByTgChatId(11L);

        assertEquals(linkDtos.size(), 3);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTgChatIdsByUrlTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, URI.create("http://mycore1"), OffsetDateTime.now());
        linkRepository.add(22L, URI.create("http://mycore1"), OffsetDateTime.now());
        linkRepository.add(11L, URI.create("http://mycore3"), OffsetDateTime.now());

        List<Long> listTgChatIds = linkRepository.findAllTgChatIdsByUrl(URI.create("http://mycore1"));

        assertEquals(11, listTgChatIds.getFirst());
        assertEquals(22, listTgChatIds.getLast());
    }
}
