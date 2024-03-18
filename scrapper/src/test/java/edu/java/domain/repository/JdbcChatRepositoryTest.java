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

    @BeforeAll
    public static void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(IntegrationTest.POSTGRES.getDriverClassName());
        dataSource.setUrl(IntegrationTest.POSTGRES.getJdbcUrl());
        dataSource.setUsername(IntegrationTest.POSTGRES.getUsername());
        dataSource.setPassword(IntegrationTest.POSTGRES.getPassword());
        chatRepository = new JdbcChatRepository(new JdbcTemplate(dataSource));
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(11);
        chatRepository.add(22);

        List<ChatDto> listOfChats = chatRepository.findAll();

        assertEquals(listOfChats.getFirst().getTgChatId(), 11);
        assertEquals(listOfChats.getLast().getTgChatId(), 22);
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        chatRepository.remove(22);

        List<ChatDto> chatDtoList = chatRepository.findAll();

        assertEquals(chatDtoList.size(), 1);
        assertEquals(chatDtoList.getFirst().getTgChatId(), 11);
    }
}
