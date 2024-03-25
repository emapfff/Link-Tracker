package edu.java.domain.jdbc;

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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class JdbcLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;
    private static LinkDto firstTuple;
    private static LinkDto secondTuple;
    private static final URI URI_MYCORE1 = URI.create("http://mycore1");
    private static final URI URI_MYCORE2 = URI.create("http://mycore2");
    private static final URI URI_MYCORE3 = URI.create("http://mycore3");
    private static final URI URI_LINK1 = URI.create("http://link1");
    private static final URI URI_LINK11 = URI.create("http://link11");

    @BeforeAll
    public static void setUp() {
        firstTuple = new LinkDto();
        firstTuple.setId(1L);
        firstTuple.setUrl(URI_MYCORE1);
        firstTuple.setLastUpdate(OffsetDateTime.now());
        secondTuple = new LinkDto();
        secondTuple.setId(2L);
        secondTuple.setUrl(URI_MYCORE2);
        secondTuple.setLastUpdate(OffsetDateTime.now());
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, URI_MYCORE1, firstTuple.getLastUpdate());
        linkRepository.add(22L, URI_MYCORE2, secondTuple.getLastUpdate());

        List<LinkDto> listOfChats = linkRepository.findAll();
        assertEquals(listOfChats.getFirst().getUrl(), URI_MYCORE1);
        assertEquals(listOfChats.getLast().getUrl(), URI_MYCORE2);
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, URI_MYCORE1, firstTuple.getLastUpdate());
        linkRepository.add(22L, URI_MYCORE2, secondTuple.getLastUpdate());

        linkRepository.remove(22L, URI_MYCORE2);

        List<LinkDto> chatDtoList = linkRepository.findAll();
        assertEquals(chatDtoList.size(), 1);
        assertEquals(chatDtoList.getFirst().getUrl(), URI_MYCORE1);
    }

    @Test
    @Transactional
    @Rollback
    void findLinkByChatIdAndUrlTest() {
        chatRepository.add(1234L);
        chatRepository.add(12345L);
        linkRepository.add(1234L, URI_MYCORE1, firstTuple.getLastUpdate());
        linkRepository.add(12345L, URI_MYCORE2, secondTuple.getLastUpdate());

        Long linkId = linkRepository.findLinkByChatIdAndUrl(12345L, URI_MYCORE2).getId();

        assertNotNull(linkId);
    }

    @Test
    @Transactional
    @Rollback
    void findAllByTgChatIdTest(){
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, URI_MYCORE1, OffsetDateTime.now());
        linkRepository.add(11L, URI_MYCORE2, OffsetDateTime.now());
        linkRepository.add(11L, URI_MYCORE3, OffsetDateTime.now());

        List<LinkDto> linkDtos = linkRepository.findAllByTgChatId(11L);

        assertEquals(linkDtos.size(), 3);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTgChatIdsByUrlTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, URI_MYCORE1, OffsetDateTime.now());
        linkRepository.add(22L, URI_MYCORE1, OffsetDateTime.now());
        linkRepository.add(11L, URI_MYCORE3, OffsetDateTime.now());

        List<Long> listTgChatIds = linkRepository.findAllTgChatIdsByUrl(URI_MYCORE1);

        assertEquals(11, listTgChatIds.getFirst());
        assertEquals(22, listTgChatIds.getLast());
    }

    @Test
    @Transactional
    @Rollback
    void setLastUpdateTest() {
        String time = "2019-08-31T15:20:30Z";
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        linkRepository.add(1L, URI_LINK1, OffsetDateTime.now());
        linkRepository.add(1L, URI_LINK11, OffsetDateTime.now());
        linkRepository.add(2L, URI_LINK1, OffsetDateTime.now());
        linkRepository.add(3L, URI_LINK1, OffsetDateTime.now());
        LinkDto link = linkRepository.findLinkByChatIdAndUrl(1L, URI_LINK1);

        linkRepository.setLastUpdate(link, OffsetDateTime.parse(time));
        System.out.println(OffsetDateTime.parse(time));

        LinkDto linkDto = linkRepository.findLinkByChatIdAndUrl(1L, URI_LINK1);
        assertEquals(linkDto.getUrl(), URI_LINK1);
        assertEquals(linkDto.getLastUpdate().toString(), time);


    }
}
