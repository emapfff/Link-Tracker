package edu.java.domain.jooq.repository;

import edu.java.domain.jooq.tables.records.ChatRecord;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JooqChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void add() {
        chatRepository.add(22L);

        List<ChatRecord> listChats = chatRepository.findAll();

        assertEquals(listChats.getLast().getTgChatId(), 22);
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        chatRepository.add(11L);
        chatRepository.add(22L);

        chatRepository.remove(22L);

        List<ChatRecord> listChats = chatRepository.findAll();
        assertEquals(listChats.getLast().getTgChatId(), 11);
    }

    @Test
    @Transactional
    @Rollback
    void existIdByTgChatId() {
        chatRepository.add(11L);
        chatRepository.add(22L);

        boolean exist = chatRepository.existIdByTgChatId(11L);

        assertTrue(exist);
    }

}
