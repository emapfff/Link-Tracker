package edu.java.domain.jooq;

import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.generation.Tables.CHAT;
import static edu.java.domain.jooq.generation.Tables.CONSISTS;
import static edu.java.domain.jooq.generation.Tables.LINK;
import static edu.java.domain.jooq.generation.Tables.STACKOVERFLOW_LINK;

@RequiredArgsConstructor
public class JooqStackOverflowLinkRepository implements StackOverflowLinkRepository {
    private final DSLContext dslContext;
    private final JooqLinkRepository jooqLinkRepository;

    @Override
    public void add(Long tgChaId, URI url, Integer answerCount) {
        Long linkId = jooqLinkRepository.findLinkByChatIdAndUrl(tgChaId, url).id();
        dslContext
            .insertInto(STACKOVERFLOW_LINK)
            .set(STACKOVERFLOW_LINK.LINK_ID, linkId)
            .set(STACKOVERFLOW_LINK.ANSWER_COUNT, answerCount)
            .execute();
    }

    @Override
    public StackOverflowDto findStackOverflowLinkByTgChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        return dslContext
            .select(STACKOVERFLOW_LINK.ID, STACKOVERFLOW_LINK.LINK_ID, STACKOVERFLOW_LINK.ANSWER_COUNT)
            .from(CHAT)
            .join(CONSISTS).on(CHAT.ID.eq(CONSISTS.CHAT_ID))
            .join(STACKOVERFLOW_LINK).on(CONSISTS.LINK_ID.eq(STACKOVERFLOW_LINK.LINK_ID))
            .join(LINK).on(LINK.ID.eq(STACKOVERFLOW_LINK.LINK_ID))
            .where(LINK.URL.eq(url.toString()).and(CHAT.TG_CHAT_ID.eq(tgChatId)))
            .fetchOneInto(StackOverflowDto.class);
    }

    @Override
    public StackOverflowDto findStackOverflowLinkByLinkId(Long linkId) {
        return dslContext
            .selectFrom(STACKOVERFLOW_LINK)
            .where(STACKOVERFLOW_LINK.LINK_ID.eq(linkId))
            .fetchOneInto(StackOverflowDto.class);
    }

    @Override
    public List<StackOverflowDto> findAll() {
        return dslContext
            .selectFrom(STACKOVERFLOW_LINK)
            .fetchInto(StackOverflowDto.class);
    }

    @Override
    public void setAnswersCount(LinkDto link, Integer answerCount) {
        dslContext
            .update(STACKOVERFLOW_LINK)
            .set(STACKOVERFLOW_LINK.ANSWER_COUNT, answerCount)
            .where(STACKOVERFLOW_LINK.LINK_ID.eq(link.id()))
            .execute();
    }
}
