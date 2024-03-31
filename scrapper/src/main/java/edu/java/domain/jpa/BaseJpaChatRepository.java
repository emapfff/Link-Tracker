package edu.java.domain.jpa;

import edu.java.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseJpaChatRepository extends JpaRepository<Chat, Long> {
    Integer countByTgChatId(Long tgChatId);
    Long findIdByTgChatId(Long tgChatId);
    void deleteChatByTgChatId(Long tgChatId);

}
