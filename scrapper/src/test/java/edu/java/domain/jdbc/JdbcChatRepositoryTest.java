package edu.java.domain.jdbc;

import edu.java.domain.dto.ChatDto;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "app.database-access-type=jdbc")
@Transactional
class JdbcChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Test
    void addTest() {
        jdbcChatRepository.add(11L);
        jdbcChatRepository.add(22L);

        List<ChatDto> listOfChats = jdbcChatRepository.findAll();

        assertEquals(listOfChats.getLast().tgChatId(), 22);
    }

    @Test
    void removeTest() {
        jdbcChatRepository.add(11L);
        jdbcChatRepository.add(22L);

        jdbcChatRepository.remove(22L);

        List<ChatDto> chatDtoList = jdbcChatRepository.findAll();
        assertEquals(chatDtoList.getLast().tgChatId(), 11);
    }

    @Test
    void existIdByTgChatIdTest() {
        Long tgChatId = 123L;
        jdbcChatRepository.add(tgChatId);

        Integer count = jdbcChatRepository.existIdByTgChatId(tgChatId);

        assertEquals(count, 1);
    }
}
