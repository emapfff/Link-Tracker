package edu.java.domain.jooq;

import edu.java.domain.LinkRepository;
import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static edu.java.domain.jooq.generation.Tables.CHAT;
import static edu.java.domain.jooq.generation.Tables.CONSISTS;
import static edu.java.domain.jooq.generation.Tables.LINK;

@Component
@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;
    @Autowired
    private JooqConsistsRepository jooqConsistsRepository;
    @Autowired
    private JooqChatRepository jooqChatRepository;

    @Override
    public void add(Long tgChatId, @NotNull URI url, @NotNull OffsetDateTime lastUpdate) {
        dslContext
            .insertInto(LINK)
            .set(LINK.URL, url.toString())
            .set(LINK.LAST_UPDATE, lastUpdate.toLocalDateTime())
            .execute();
        Long chaId = jooqChatRepository.findIdByTgChatId(tgChatId);
        Long linkId = findAllByUrl(url).getLast().id();
        jooqConsistsRepository.add(chaId, linkId);
    }

    @Override
    public List<LinkDto> findAllByUrl(@NotNull URI url) {
        return dslContext
            .selectFrom(LINK)
            .where(LINK.URL.eq(url.toString()))
            .fetchInto(LinkDto.class);
    }

    @Override
    public void remove(Long tgChatId, URI url) {
        Long linkId = findLinkByChatIdAndUrl(tgChatId, url).id();
        dslContext
            .deleteFrom(LINK)
            .where(LINK.URL.eq(url.toString()).and(LINK.ID.eq(linkId)))
            .execute();
    }

    @Override
    public LinkDto findLinkByChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        return Objects.requireNonNull(dslContext
                .select(LINK.ID, LINK.URL, LINK.LAST_UPDATE)
                .from(LINK)
                .join(CONSISTS).on(LINK.ID.eq(CONSISTS.LINK_ID))
                .join(CHAT).on(CHAT.ID.eq(CONSISTS.CHAT_ID))
                .where(CHAT.TG_CHAT_ID.eq(tgChatId).and(LINK.URL.eq(url.toString())))
                .fetchOne())
            .map(this::link);
    }

    @Override
    public Integer existLinkByUriAndTgChatId(Long tgChatId, @NotNull URI url) {
        return dslContext
            .selectCount()
            .from(LINK)
            .join(CONSISTS).on(LINK.ID.eq(CONSISTS.LINK_ID))
            .join(CHAT).on(CHAT.ID.eq(CONSISTS.CHAT_ID))
            .where(CHAT.TG_CHAT_ID.eq(tgChatId).and(LINK.URL.eq(url.toString())))
            .fetchOneInto(Integer.class);
    }

    @Override
    public List<LinkDto> findAll() {
        return dslContext
            .selectFrom(LINK)
            .fetchInto(LinkDto.class);
    }

    @Override
    public List<LinkDto> findAllByTgChatId(Long tgChatId) {
        return dslContext
            .select(LINK.ID, LINK.URL, LINK.LAST_UPDATE)
            .from(LINK)
            .join(CONSISTS).on(LINK.ID.eq(CONSISTS.LINK_ID))
            .join(CHAT).on(CHAT.ID.eq(CONSISTS.CHAT_ID))
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .fetch()
            .map(this::link);
    }

    @Override
    public List<Long> findAllTgChatIdsByUrl(@NotNull URI url) {
        return dslContext
            .select(CHAT.TG_CHAT_ID)
            .from(LINK)
            .join(CONSISTS).on(LINK.ID.eq(CONSISTS.LINK_ID))
            .join(CHAT).on(CHAT.ID.eq(CONSISTS.CHAT_ID))
            .where(LINK.URL.eq(url.toString()))
            .fetchInto(Long.class);
    }

    @Override
    public void setLastUpdate(@NotNull LinkDto linkDto, @NotNull OffsetDateTime lastUpdate) {
        dslContext
            .update(LINK)
            .set(LINK.LAST_UPDATE, lastUpdate.toLocalDateTime())
            .where(LINK.ID.eq(linkDto.id()).and(LINK.URL.eq(linkDto.url().toString())))
            .execute();
    }

    @Contract("_ -> new")
    private @NotNull LinkDto link(Record r) {
        return new LinkDto(
            r.get(LINK.ID),
            URI.create(r.get(LINK.URL)),
            OffsetDateTime.ofInstant(
                r.get(LINK.LAST_UPDATE).toInstant(ZoneOffset.ofHours(0)),
                ZoneOffset.ofHours(0)
            )
        );
    }

}
