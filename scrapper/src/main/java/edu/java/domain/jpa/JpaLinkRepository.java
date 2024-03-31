package edu.java.domain.jpa;

import edu.java.domain.LinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.entity.Chat;
import edu.java.domain.entity.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JpaLinkRepository implements LinkRepository {
    @Autowired
    private BaseJpaLinkRepository baseJpaLinkRepository;

    @Override
    public void add(Long tgChatId, @NotNull URI url, @NotNull OffsetDateTime lastUpdate) {
        Link link = new Link(url.toString(), lastUpdate.toLocalDateTime());
        link.getChats().add(new Chat(tgChatId));
        baseJpaLinkRepository.save(new Link(url.toString(), lastUpdate.toLocalDateTime()));
    }

    @Override
    public List<LinkDto> findAllByUrl(@NotNull URI url) {
        return baseJpaLinkRepository.findAllByUrl(url.toString()).stream()
            .map(this::linkDto)
            .collect(Collectors.toList());
    }

    @Override
    public void remove(Long tgChatId, URI url) {

    }

    @Override
    public LinkDto findLinkByChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        return null;
    }

    @Override
    public Integer existLinkByUriAndTgChatId(Long tgChatId, @NotNull URI url) {
        return null;
    }

    @Override
    public List<LinkDto> findAll() {
        return null;
    }

    @Override
    public List<LinkDto> findAllByTgChatId(Long tgChatId) {
        return null;
    }

    @Override
    public List<Long> findAllTgChatIdsByUrl(@NotNull URI url) {
        return null;
    }

    @Override
    public void setLastUpdate(@NotNull LinkDto link, OffsetDateTime lastUpdate) {

    }

    @Contract("_-> new")
    private @NotNull LinkDto linkDto(Link link) {
        return new LinkDto(
            link.getId(),
            URI.create(link.getUrl()),
            OffsetDateTime.ofInstant(
                link.getLastUpdate().toInstant(ZoneOffset.ofHours(0)),
                ZoneOffset.ofHours(0)
            )
        );
    }
}
