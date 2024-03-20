package edu.java.domain.repository;

import edu.java.domain.dto.LinkDto;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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
        firstTuple.setId(1);
        firstTuple.setUrl(URI.create("http://mycore1"));
        firstTuple.setLastUpdate(OffsetDateTime.now());
        secondTuple = new LinkDto();
        secondTuple.setId(2);
        secondTuple.setUrl(URI.create("http://mycore2"));
        secondTuple.setLastUpdate(OffsetDateTime.now());
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(11);
        chatRepository.add(22);
        linkRepository.add(11, firstTuple.getUrl(), firstTuple.getLastUpdate());
        linkRepository.add(22, secondTuple.getUrl(), secondTuple.getLastUpdate());


        List<LinkDto> listOfChats = linkRepository.findAll();
        assertEquals(listOfChats.getFirst().getUrl(), firstTuple.getUrl());
        System.out.println((listOfChats.getFirst().getLastUpdate()));
        System.out.println(firstTuple.getLastUpdate());
        assertEquals(listOfChats.getFirst().getLastUpdate().toLocalDate(), firstTuple.getLastUpdate().toLocalDate());
        assertEquals(listOfChats.getLast().getUrl(), secondTuple.getUrl());
        assertEquals(listOfChats.getLast().getLastUpdate().toLocalDate(), secondTuple.getLastUpdate().toLocalDate());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() throws SQLException {
        chatRepository.add(11);
        chatRepository.add(22);
        linkRepository.add(11, firstTuple.getUrl(), firstTuple.getLastUpdate());
        linkRepository.add(22, secondTuple.getUrl(), secondTuple.getLastUpdate());
        URI link = URI.create("http://mycore2");

        linkRepository.remove(22, link);

        List<LinkDto> chatDtoList = linkRepository.findAll();
        assertEquals(chatDtoList.size(), 1);
        assertEquals(chatDtoList.getFirst().getUrl(), URI.create("http://mycore1"));
    }

    @Test
    @Transactional
    @Rollback
    void findLinkIdByChatIdAndUrlTest() throws SQLException {
        chatRepository.add(11);
        chatRepository.add(22);
        linkRepository.add(11, firstTuple.getUrl(), firstTuple.getLastUpdate());
        linkRepository.add(22, secondTuple.getUrl(), secondTuple.getLastUpdate());

        Integer linkId = linkRepository.findLinkIdByChatIdAndUrl(11, URI.create("http://mycore1")).getId();

        assertEquals(linkId, 1);
    }

    @Test
    @Transactional
    @Rollback
    void findAllByTgChatIdTest(){
        chatRepository.add(11);
        chatRepository.add(22);
        linkRepository.add(11, URI.create("http://mycore1"), OffsetDateTime.now());
        linkRepository.add(11, URI.create("http://mycore2"), OffsetDateTime.now());
        linkRepository.add(11, URI.create("http://mycore3"), OffsetDateTime.now());

        List<LinkDto> linkDtos = linkRepository.findAllByTgChatId(11);

        assertEquals(linkDtos.size(), 3);

    }
}
