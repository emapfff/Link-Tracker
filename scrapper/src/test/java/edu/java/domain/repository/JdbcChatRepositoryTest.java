package edu.java.domain.repository;

import edu.java.domain.dto.ChatDto;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JdbcChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);

        List<ChatDto> listOfChats = chatRepository.findAll();

        assertEquals(listOfChats.getFirst().getTgChatId(), 11);
        assertEquals(listOfChats.getLast().getTgChatId(), 22);
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        chatRepository.add(11L);
        chatRepository.add(22L);

        chatRepository.remove(22L);

        List<ChatDto> chatDtoList = chatRepository.findAll();

        assertEquals(chatDtoList.size(), 1);
        assertEquals(chatDtoList.getFirst().getTgChatId(), 11);
    }
}
