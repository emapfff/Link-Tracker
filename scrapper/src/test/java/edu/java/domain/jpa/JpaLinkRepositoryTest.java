package edu.java.domain.jpa;

import edu.java.domain.dto.LinkDto;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "app.database-access-type=jpa")
@Transactional
class JpaLinkRepositoryTest extends IntegrationTest {
    private static final URI URI_MYCORE1 = URI.create("http://mycore1");
    private static final URI URI_MYCORE2 = URI.create("http://mycore2");
    private static final URI URI_MYCORE3 = URI.create("http://mycore3");
    private static final URI URI_LINK1 = URI.create("http://link1");
    private static final URI URI_LINK11 = URI.create("http://link11");
    private static LinkDto firstTuple;
    private static LinkDto secondTuple;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;
    @Autowired
    private JpaChatRepository jpaChatRepository;

    @BeforeAll
    public static void setUp() {
        firstTuple = new LinkDto(1L, URI_MYCORE1, OffsetDateTime.now());
        secondTuple = new LinkDto(2L, URI_MYCORE2, OffsetDateTime.now());
    }

    @Test
    void add() {
        jpaChatRepository.add(11L);
        jpaChatRepository.add(22L);
        jpaLinkRepository.add(22L, URI_MYCORE2, secondTuple.lastUpdate());

        List<LinkDto> listOfChats = jpaLinkRepository.findAll();

        assertEquals(listOfChats.getLast().url(), URI_MYCORE2);
        assertEquals(jpaChatRepository.getChat().getFirst().getLinks().size(), 0);
        assertEquals(jpaChatRepository.getChat().getLast().getLinks().size(), 1);
    }

    @Test
    void removeTest() {
        jpaChatRepository.add(11L);
        jpaChatRepository.add(22L);
        jpaLinkRepository.add(11L, URI_MYCORE1, firstTuple.lastUpdate());
        jpaLinkRepository.add(22L, URI_MYCORE2, secondTuple.lastUpdate());
        jpaLinkRepository.add(22L, URI_MYCORE3, secondTuple.lastUpdate());

        jpaLinkRepository.remove(22L, URI_MYCORE2);

        List<LinkDto> linkDtos = jpaLinkRepository.findAll();
        assertEquals(linkDtos.getLast().url(), URI_MYCORE3);
    }

    @Test
    void findLinkByChatIdAndUrlTest() {
        jpaChatRepository.add(1234L);
        jpaChatRepository.add(12345L);

        jpaLinkRepository.add(1234L, URI_MYCORE1, firstTuple.lastUpdate());
        jpaLinkRepository.add(12345L, URI_MYCORE2, secondTuple.lastUpdate());

        Long linkId = jpaLinkRepository.findLinkByChatIdAndUrl(12345L, URI_MYCORE2).id();

        assertNotNull(linkId);
    }

    @Test
    void findAllByTgChatIdTest() {
        jpaChatRepository.add(11L);
        jpaChatRepository.add(22L);
        jpaLinkRepository.add(11L, URI_MYCORE1, OffsetDateTime.now());
        jpaLinkRepository.add(11L, URI_MYCORE2, OffsetDateTime.now());
        jpaLinkRepository.add(11L, URI_MYCORE3, OffsetDateTime.now());

        List<LinkDto> links = jpaLinkRepository.findAllByTgChatId(11L);

        assertEquals(links.size(), 3);
    }

    @Test
    void findAllTgChatIdsByUrlTest() {
        jpaChatRepository.add(11L);
        jpaChatRepository.add(22L);
        jpaLinkRepository.add(11L, URI_MYCORE1, OffsetDateTime.now());
        jpaLinkRepository.add(22L, URI_MYCORE1, OffsetDateTime.now());
        jpaLinkRepository.add(11L, URI_MYCORE3, OffsetDateTime.now());

        List<Long> listTgChatIds = jpaLinkRepository.findAllTgChatIdsByUrl(URI_MYCORE1);
        assertEquals(22, listTgChatIds.getLast());
    }

    @Test
    void existLinkByUriAndTgChatId() {
        jpaChatRepository.add(111L);
        jpaChatRepository.add(222L);
        jpaLinkRepository.add(111L, URI_MYCORE1, OffsetDateTime.now());
        jpaLinkRepository.add(222L, URI_MYCORE1, OffsetDateTime.now());
        jpaLinkRepository.add(111L, URI_MYCORE3, OffsetDateTime.now());

        Integer count = jpaLinkRepository.existLinkByUriAndTgChatId(0L, URI_MYCORE1);

        assertEquals(count, 0);
    }

    @Test
    void setLastUpdateTest() {
        String time = "2019-08-31T15:20:30Z";
        jpaChatRepository.add(1L);
        jpaChatRepository.add(2L);
        jpaChatRepository.add(3L);
        jpaLinkRepository.add(1L, URI_LINK1, OffsetDateTime.now());
        jpaLinkRepository.add(1L, URI_LINK11, OffsetDateTime.now());
        jpaLinkRepository.add(2L, URI_LINK1, OffsetDateTime.now());
        jpaLinkRepository.add(3L, URI_LINK1, OffsetDateTime.now());
        LinkDto link = jpaLinkRepository.findLinkByChatIdAndUrl(1L, URI_LINK1);

        jpaLinkRepository.setLastUpdate(link, OffsetDateTime.parse(time));

        LinkDto linkDto = jpaLinkRepository.findLinkByChatIdAndUrl(1L, URI_LINK1);

        assertEquals(linkDto.url(), URI_LINK1);
        assertEquals(linkDto.lastUpdate().toString(), time);
    }

}
