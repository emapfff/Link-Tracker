package edu.java.domain.jooq;

import edu.java.domain.GithubLinkRepository;
import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static edu.java.domain.jooq.generation.Tables.CHAT;
import static edu.java.domain.jooq.generation.Tables.CONSISTS;
import static edu.java.domain.jooq.generation.Tables.GITHUB_LINKS;
import static edu.java.domain.jooq.generation.Tables.LINK;

@Component
@RequiredArgsConstructor
public class JooqGithubLinkRepository implements GithubLinkRepository {
    private final DSLContext dslContext;
    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    @Override
    public void add(Long tgChatId, URI url, Integer countBranches) {
        Long linkId = jooqLinkRepository.findLinkByChatIdAndUrl(tgChatId, url).id();
        dslContext
            .insertInto(GITHUB_LINKS)
            .set(GITHUB_LINKS.LINK_ID, linkId)
            .set(GITHUB_LINKS.COUNT_BRANCHES, countBranches)
            .execute();
    }

    @Override
    public GithubLinkDto findGithubLinkByTgChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        return dslContext
            .select(GITHUB_LINKS.ID, GITHUB_LINKS.LINK_ID, GITHUB_LINKS.COUNT_BRANCHES)
            .from(CHAT)
            .join(CONSISTS).on(CHAT.ID.eq(CONSISTS.CHAT_ID))
            .join(GITHUB_LINKS).on(CONSISTS.LINK_ID.eq(GITHUB_LINKS.LINK_ID))
            .join(LINK).on(LINK.ID.eq(GITHUB_LINKS.LINK_ID))
            .where(LINK.URL.eq(url.toString()).and(CHAT.TG_CHAT_ID.eq(tgChatId)))
            .fetchOneInto(GithubLinkDto.class);
    }

    @Override
    public GithubLinkDto findGithubLinkByLinkId(Long linkId) {
        return dslContext
            .selectFrom(GITHUB_LINKS)
            .where(GITHUB_LINKS.LINK_ID.eq(linkId))
            .fetchOneInto(GithubLinkDto.class);
    }

    @Override
    public List<GithubLinkDto> findAll() {
        return dslContext
            .selectFrom(GITHUB_LINKS)
            .fetchInto(GithubLinkDto.class);
    }

    @Override
    public void setCountBranches(@NotNull LinkDto link, Integer countBranches) {
        dslContext
            .update(GITHUB_LINKS)
            .set(GITHUB_LINKS.COUNT_BRANCHES, countBranches)
            .where(GITHUB_LINKS.LINK_ID.eq(link.id()))
            .execute();

    }

}
