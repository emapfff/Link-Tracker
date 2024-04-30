package edu.java.domain.jooq;

import edu.java.domain.ChatRepository;
import edu.java.domain.dto.ChatDto;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import static edu.java.domain.jooq.generation.tables.Chat.CHAT;

@Component
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {

    private final DSLContext dslContext;

    @Override
    public void add(Long tgChatId) {
        dslContext
            .insertInto(CHAT)
            .set(CHAT.TG_CHAT_ID, tgChatId)
            .execute();
    }

    @Override
    public void remove(Long tgChatId) {
        dslContext.deleteFrom(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .execute();
    }

    @Override
    public Integer existIdByTgChatId(Long tgChatId) {
        return Objects.requireNonNull(dslContext.selectCount()
                .from(CHAT)
                .where(CHAT.TG_CHAT_ID.eq(tgChatId))
                .fetchOne())
            .value1();
    }

    @Override
    public Long findIdByTgChatId(Long tgChatId) {
        return dslContext.select(CHAT.ID)
            .from(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .fetchOneInto(Long.class);
    }

    @Override
    public List<ChatDto> findAll() {
        return dslContext
            .selectFrom(CHAT)
            .fetchInto(ChatDto.class);
    }
}
