package edu.java.domain.jdbc;

import edu.java.domain.ChatRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.dto.ChatDto;
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

@SpringBootTest(properties = "app.database-access-type=jdbc")
@Transactional
class JdbcLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatRepository chatRepository;
    private static LinkDto firstTuple;
    private static LinkDto secondTuple;
    private static final URI URI_MYCORE1 = URI.create("http://mycore1");
    private static final URI URI_MYCORE2 = URI.create("http://mycore2");
    private static final URI URI_MYCORE3 = URI.create("http://mycore3");
    private static final URI URI_LINK1 = URI.create("http://link1");
    private static final URI URI_LINK11 = URI.create("http://link11");

    @BeforeAll
    public static void setUp() {
        firstTuple = new LinkDto(1L, URI_MYCORE1, OffsetDateTime.now());
        secondTuple = new LinkDto(2L, URI_MYCORE2, OffsetDateTime.now());
    }

    @Test
    void addTest() {
        chatRepository.add(22L);
        linkRepository.add(22L, URI_MYCORE2, secondTuple.lastUpdate());

        List<LinkDto> listOfChats = linkRepository.findAll();
        assertEquals(listOfChats.getLast().url(), URI_MYCORE2);
    }

    @Test
    void removeTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);
        List<ChatDto> dtoList = chatRepository.findAll();
        dtoList.forEach(System.out::println);
        linkRepository.add(11L, URI_MYCORE1, firstTuple.lastUpdate());
        linkRepository.add(22L, URI_MYCORE2, secondTuple.lastUpdate());

        linkRepository.remove(22L, URI_MYCORE2);

        List<LinkDto> chatDtoList = linkRepository.findAll();
        assertEquals(chatDtoList.getLast().url(), URI_MYCORE1);
    }

    @Test
    void findLinkByChatIdAndUrlTest() {
        chatRepository.add(1234L);
        chatRepository.add(12345L);
        linkRepository.add(1234L, URI_MYCORE1, firstTuple.lastUpdate());
        linkRepository.add(12345L, URI_MYCORE2, secondTuple.lastUpdate());

        Long linkId = linkRepository.findLinkByChatIdAndUrl(12345L, URI_MYCORE2).id();

        assertNotNull(linkId);
    }

    @Test
    void findAllByTgChatIdTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, URI_MYCORE1, OffsetDateTime.now());
        linkRepository.add(11L, URI_MYCORE2, OffsetDateTime.now());
        linkRepository.add(11L, URI_MYCORE3, OffsetDateTime.now());

        List<LinkDto> linkDtos = linkRepository.findAllByTgChatId(11L);

        assertEquals(linkDtos.size(), 3);
    }

    @Test
    void findAllTgChatIdsByUrlTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);
        linkRepository.add(11L, URI_MYCORE1, OffsetDateTime.now());
        linkRepository.add(22L, URI_MYCORE1, OffsetDateTime.now());
        linkRepository.add(11L, URI_MYCORE3, OffsetDateTime.now());

        List<Long> listTgChatIds = linkRepository.findAllTgChatIdsByUrl(URI_MYCORE1);

        assertEquals(22, listTgChatIds.getLast());
    }

    @Test
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

        LinkDto linkDto = linkRepository.findLinkByChatIdAndUrl(1L, URI_LINK1);
        assertEquals(linkDto.url(), URI_LINK1);
        assertEquals(linkDto.lastUpdate().toString(), time);
    }
}
