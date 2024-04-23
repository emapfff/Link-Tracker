package edu.java.domain.jpa;

import edu.java.domain.dto.ChatDto;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties = "app.database-access-type=jpa")
@Transactional
class JpaChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JpaChatRepository jpaChatRepository;
    @Test
    void add() {
        jpaChatRepository.add(11L);
        jpaChatRepository.add(22L);

        List<ChatDto> chatDtoList = jpaChatRepository.findAll();
        assertEquals(chatDtoList.getLast().tgChatId(), 22);
    }

    @Test
    void remove() {
        jpaChatRepository.add(11L);
        jpaChatRepository.add(22L);

        jpaChatRepository.remove(22L);

        List<ChatDto> listChats = jpaChatRepository.findAll();
        assertEquals(listChats.getLast().tgChatId(), 11);
    }

    @Test
    void existIdByTgChatId() {
        jpaChatRepository.add(11L);
        jpaChatRepository.add(22L);

        Integer exist = jpaChatRepository.existIdByTgChatId(11L);

        assertEquals(exist, 1);
    }

    @Test
    void findIdByTgChatId() {
        jpaChatRepository.add(11L);
        jpaChatRepository.add(22L);

        Long id = jpaChatRepository.findIdByTgChatId(11L);

        assertNotNull(id);
    }

}
