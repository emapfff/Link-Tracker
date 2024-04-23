package edu.java.domain.jpa.bases;

import edu.java.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BaseJpaChatRepository extends JpaRepository<Chat, Long> {
    Integer countByTgChatId(Long tgChatId);

    Chat findChatByTgChatId(Long tgChatId);

    void deleteChatByTgChatId(Long tgChatId);
}
