package edu.java.domain.jdbc;

import edu.java.configuration.DataSourceConfig;
import edu.java.configuration.JdbcTemplateConfig;
import edu.java.configuration.TransactionManagerConfig;
import edu.java.domain.dto.ChatDto;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {JdbcChatRepository.class})
@ContextConfiguration(classes = {JdbcTemplateConfig.class, DataSourceConfig.class, TransactionManagerConfig.class})
@Transactional
@Rollback
class JdbcChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    void addTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);

        List<ChatDto> listOfChats = chatRepository.findAll();

        assertEquals(listOfChats.getLast().tgChatId(), 22);
    }

    @Test
    void removeTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);

        chatRepository.remove(22L);

        List<ChatDto> chatDtoList = chatRepository.findAll();
        assertEquals(chatDtoList.getLast().tgChatId(), 11);
    }

    @Test
    void existIdByTgChatIdTest() {
        Long tgChatId = 123L;
        chatRepository.add(tgChatId);

        Integer count = chatRepository.existIdByTgChatId(tgChatId);

        assertEquals(count, 1);
    }
}
