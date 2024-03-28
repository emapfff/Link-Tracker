package edu.java.domain.jooq.repository;

import edu.java.domain.jooq.tables.records.ChatRecord;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.Chat.CHAT;

@Repository
@AllArgsConstructor
public class JooqChatRepository {

    private DSLContext dslContext;

    public void add(Long tgChatId) {
        dslContext
            .insertInto(CHAT)
            .set(CHAT.TG_CHAT_ID, tgChatId)
            .execute();
    }

    public void remove(Long tgChatId) {
        dslContext.deleteFrom(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .execute();
    }

    public boolean existIdByTgChatId(Long tgChatId) {
        return dslContext.selectOne()
            .from(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .fetchOptional()
            .isPresent();
    }

    public Long findIdByTgChatId(Long tgChatId) {
        return dslContext.select(CHAT.ID)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .fetchOne(0, Long.class);
    }

    public List<ChatRecord> findAll() {
        return dslContext.selectFrom(CHAT).fetch();
    }
}
