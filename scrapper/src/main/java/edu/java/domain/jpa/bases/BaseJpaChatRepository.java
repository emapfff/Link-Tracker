package edu.java.domain.jpa.bases;

import edu.java.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BaseJpaChatRepository extends JpaRepository<Chat, Long> {
    Integer countByTgChatId(Long tgChatId);

    @Query("select c.id from Chat c where c.tgChatId=?1")
    Long findIdByTgChatId(Long tgChatId);

    @Query("select c from Chat c where c.tgChatId= :tgChatId")
    Chat findChatByTgChatId(Long tgChatId);

    void deleteChatByTgChatId(Long tgChatId);
}
