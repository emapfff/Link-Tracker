package edu.java.domain.jooq;

import edu.java.configuration.JooqConfig;
import edu.java.domain.dto.ChatDto;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "app.database-access-type=jooq")
@Transactional
class JooqChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatRepository jooqChatRepository;

    @Test
    void add() {
        jooqChatRepository.add(22L);

        List<ChatDto> listChats = jooqChatRepository.findAll();
        System.out.println(listChats);
        assertEquals(listChats.getLast().tgChatId(), 22);
    }

    @Test
    void remove() {
        jooqChatRepository.add(11L);
        jooqChatRepository.add(22L);

        jooqChatRepository.remove(22L);

        List<ChatDto> listChats = jooqChatRepository.findAll();
        assertEquals(listChats.getLast().tgChatId(), 11);
    }

    @Test
    void existIdByTgChatId() {
        jooqChatRepository.add(11L);
        jooqChatRepository.add(22L);

        Integer exist = jooqChatRepository.existIdByTgChatId(11L);

        assertEquals(exist, 1);
    }

    @Test
    void findIdByTgChatId() {
        jooqChatRepository.add(11L);
        jooqChatRepository.add(22L);

        Long id = jooqChatRepository.findIdByTgChatId(11L);

        assertNotNull(id);
    }

}
