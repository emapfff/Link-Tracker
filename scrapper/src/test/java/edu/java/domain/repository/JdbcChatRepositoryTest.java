package edu.java.domain.repository;

import edu.java.domain.dto.ChatDto;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcChatRepositoryTest extends IntegrationTest {

    private static JdbcChatRepository chatRepository;

    private static ChatDto firstTuple;
    private static ChatDto secondTuple;

    @BeforeAll
    public static void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(IntegrationTest.POSTGRES.getDriverClassName());
        dataSource.setUrl(IntegrationTest.POSTGRES.getJdbcUrl());
        dataSource.setUsername(IntegrationTest.POSTGRES.getUsername());
        dataSource.setPassword(IntegrationTest.POSTGRES.getPassword());
        chatRepository = new JdbcChatRepository(new JdbcTemplate(dataSource));
        firstTuple = new ChatDto();
        firstTuple.setId(1);
        firstTuple.setUserName("Emil");
        firstTuple.setCreatedAt(OffsetDateTime.now());
        secondTuple = new ChatDto();
        secondTuple.setId(2);
        secondTuple.setUserName("Davlityarov");
        secondTuple.setCreatedAt(OffsetDateTime.now());
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(firstTuple);
        chatRepository.add(secondTuple);

        List<ChatDto> listOfChats = chatRepository.findAll();

        assertEquals(listOfChats.getFirst().getId(), firstTuple.getId());
        assertEquals(listOfChats.getFirst().getUserName(), firstTuple.getUserName());
        assertEquals(listOfChats.getFirst().getCreatedAt().toLocalDate(), firstTuple.getCreatedAt().toLocalDate());
        assertEquals(listOfChats.getLast().getId(), secondTuple.getId());
        assertEquals(listOfChats.getLast().getUserName(), secondTuple.getUserName());
        assertEquals(listOfChats.getLast().getCreatedAt().toLocalDate(), secondTuple.getCreatedAt().toLocalDate());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        chatRepository.remove(2);

        List<ChatDto> chatDtoList = chatRepository.findAll();

        assertEquals(chatDtoList.size(), 1);
        assertEquals(chatDtoList.getFirst().getId(), 1);
    }
}
