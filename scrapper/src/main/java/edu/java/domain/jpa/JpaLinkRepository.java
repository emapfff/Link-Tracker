package edu.java.domain.jpa;

import edu.java.domain.LinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.entity.Chat;
import edu.java.domain.entity.Link;
import edu.java.domain.jpa.bases.BaseJpaChatRepository;
import edu.java.domain.jpa.bases.BaseJpaLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class JpaLinkRepository implements LinkRepository {
    private final BaseJpaLinkRepository baseJpaLinkRepository;

    private final BaseJpaChatRepository baseJpaChatRepository;

    @Override
    public void add(Long tgChatId, @NotNull URI url, @NotNull OffsetDateTime lastUpdate) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        Link link = new Link(url.toString(), lastUpdate.toLocalDateTime());
        chat.addLink(link);
    }

    @Override
    public List<LinkDto> findAllByUrl(@NotNull URI url) {
        List<Link> links = baseJpaLinkRepository.findAllByUrl(url.toString());
        return links.stream()
            .map(this::convertToLinkDto)
            .collect(Collectors.toList());
    }

    @Override
    public void remove(Long tgChatId, @NotNull URI url) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        Link link = baseJpaLinkRepository.findLinkByChatsAndUrl(chat, url.toString());
        chat.removeLink(link);
        baseJpaLinkRepository.delete(link);
    }

    @Override
    public LinkDto findLinkByChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        return convertToLinkDto(baseJpaLinkRepository.findLinkByChatsAndUrl(chat, url.toString()));
    }

    @Override
    public Integer existLinkByUriAndTgChatId(Long tgChatId, @NotNull URI url) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        return baseJpaLinkRepository.countLinkByUrlAndChats(chat, url.toString());
    }

    @Override
    public List<LinkDto> findAll() {
        return baseJpaLinkRepository.findAll().stream()
            .map(this::convertToLinkDto)
            .collect(Collectors.toList());
    }

    public List<Link> getLinks() {
        return baseJpaLinkRepository.findAll();
    }

    @Override
    public List<LinkDto> findAllByTgChatId(Long tgChatId) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        return baseJpaLinkRepository.findAllByChats(chat).stream()
            .map(this::convertToLinkDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<Long> findAllTgChatIdsByUrl(@NotNull URI url) {
        return baseJpaLinkRepository.findTgChatsIdsByUrl(url.toString());
    }

    @Override
    public void setLastUpdate(@NotNull LinkDto linkDto, @NotNull OffsetDateTime lastUpdate) {
        Link link = baseJpaLinkRepository.findLinkByIdAndUrl(linkDto.id(), linkDto.url().toString());
        link.setLastUpdate(lastUpdate.toLocalDateTime());
    }

    @Contract("_->new")
    private @NotNull LinkDto convertToLinkDto(@NotNull Link link) {
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
