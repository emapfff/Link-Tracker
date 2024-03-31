package edu.java.domain.jpa;

import edu.java.domain.entity.Link;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BaseJpaLinkRepository extends JpaRepository<Link, Long> {
    List<Link> findAllByUrl(String url);

    @Query("select COUNT (*) from Link l join Link .chats")
    Integer countLinkByUrlAndTgChatId(Long tgChatId, String url);

    @Modifying
    @Query("update Link l set l.lastUpdate = :lastUpdate where l.id = :id")
    void setLastUpdate(Long id, LocalDateTime lastUpdate);
}
