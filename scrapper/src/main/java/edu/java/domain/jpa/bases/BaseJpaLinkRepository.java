package edu.java.domain.jpa.bases;

import edu.java.domain.entity.Chat;
import edu.java.domain.entity.Link;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BaseJpaLinkRepository extends JpaRepository<Link, Long> {
    @Query("SELECT l FROM Link l WHERE l.url = :url")
    List<Link> findAllByUrl(String url);

    @Query("SELECT l FROM Link l WHERE :chat MEMBER OF l.chats AND l.url = :url")
    Link findLinkByChatsAndUrl(Chat chat, String url);



    @Query("SELECT l FROM Link l WHERE :chat MEMBER OF l.chats")
    List<Link> findAllByChats(Chat chat);

    @Query("SELECT c.tgChatId FROM Link l JOIN l.chats c WHERE l.url = :url")
    List<Long> findTgChatsIdsByUrl(String url);

    @Query("SELECT COUNT(*) FROM Link l WHERE :chat MEMBER OF l.chats AND l.url = :url")
    Integer countLinkByUrlAndChats(Chat chat, String url);

    @Query("SELECT l FROM Link l WHERE l.id = :id AND l.url = :url")
    Link findLinkByIdAndUrl(Long id, String url);

    @Query("SELECT l.id FROM Link l WHERE l.id = :id")
    Link findLinkById(Long id);
}
