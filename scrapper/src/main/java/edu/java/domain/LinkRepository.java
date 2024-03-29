package edu.java.domain;

import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface LinkRepository {
    void add(Long tgChatId, @NotNull URI url, @NotNull OffsetDateTime lastUpdate);

    List<LinkDto> findAllByUrl(@NotNull URI url);

    void remove(Long tgChatId, URI url);

    LinkDto findLinkByChatIdAndUrl(Long tgChatId, @NotNull URI url);

    Integer existLinkByUriAndTgChatId(Long tgChatId, @NotNull URI url);

    List<LinkDto> findAll();

    List<LinkDto> findAllByTgChatId(Long tgChatId);

    List<Long> findAllTgChatIdsByUrl(@NotNull URI url);

    void setLastUpdate(@NotNull LinkDto link, OffsetDateTime lastUpdate);
}
