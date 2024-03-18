package edu.java.domain.repository;

import edu.java.domain.dto.ChatDto;
import edu.java.domain.dto.LinkDto;
import edu.java.scrapper.IntegrationTest;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcLinkRepositoryTest extends IntegrationTest {
    private static JdbcLinkRepository linkRepository;
    private static LinkDto firstTuple;
    private static LinkDto secondTuple;

    @BeforeAll
    public static void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(IntegrationTest.POSTGRES.getDriverClassName());
        dataSource.setUrl(IntegrationTest.POSTGRES.getJdbcUrl());
        dataSource.setUsername(IntegrationTest.POSTGRES.getUsername());
        dataSource.setPassword(IntegrationTest.POSTGRES.getPassword());
        linkRepository = new JdbcLinkRepository(new JdbcTemplate(dataSource));
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
        linkRepository.add(firstTuple);
        linkRepository.add(secondTuple);

        List<LinkDto> listOfChats = linkRepository.findAll();

        assertEquals(listOfChats.getFirst().getId(), firstTuple.getId());
        assertEquals(listOfChats.getFirst().getUrl(), firstTuple.getUrl());
        assertEquals(listOfChats.getFirst().getLastUpdate().toLocalDate(), firstTuple.getLastUpdate().toLocalDate());
        assertEquals(listOfChats.getLast().getId(), secondTuple.getId());
        assertEquals(listOfChats.getLast().getUrl(), secondTuple.getUrl());
        assertEquals(listOfChats.getLast().getLastUpdate().toLocalDate(), secondTuple.getLastUpdate().toLocalDate());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        linkRepository.remove(2);

        List<LinkDto> chatDtoList = linkRepository.findAll();

        assertEquals(chatDtoList.size(), 1);
        assertEquals(chatDtoList.getFirst().getId(), 1);
    }
}
