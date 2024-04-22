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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JpaLinkRepository implements LinkRepository {
    @Autowired
    private BaseJpaLinkRepository baseJpaLinkRepository;
    @Autowired
    private BaseJpaChatRepository baseJpaChatRepository;

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

    public Link findLinkByChatAndUrl(@NotNull Chat chat, String url) {
        return chat.getLinks().stream()
            .filter(link -> link.getUrl().equals(url))
            .findFirst()
            .orElse(null);
    }

    @Override
    public void remove(Long tgChatId, @NotNull URI url) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        Link link = findLinkByChatAndUrl(chat, url.toString());
        chat.removeLink(link);
        baseJpaLinkRepository.delete(link);
    }

    @Override
    public LinkDto findLinkByChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        return convertToLinkDto(findLinkByChatAndUrl(chat, url.toString()));
    }

    public Integer countLinkByUrlAndChat(Chat chat, String url) {
        return Math.toIntExact(baseJpaLinkRepository.findAll().stream()
            .filter(link -> link.getUrl().equals(url) && link.getChats().contains(chat))
            .count());
    }

    @Override
    public Integer existLinkByUriAndTgChatId(Long tgChatId, @NotNull URI url) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        return countLinkByUrlAndChat(chat, url.toString());
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

    public List<LinkDto> findLinksByChat(Chat chat) {
        return baseJpaLinkRepository.findAll().stream()
            .filter(link -> link.getChats().contains(chat))
            .map(this::convertToLinkDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<LinkDto> findAllByTgChatId(Long tgChatId) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        return findLinksByChat(chat);
    }

    @Override
    public List<Long> findAllTgChatIdsByUrl(@NotNull URI url) {
        return baseJpaLinkRepository.findAll().stream()
            .filter(link -> link.getUrl().equals(url.toString()))
            .flatMap(link -> link.getChats() != null ? link.getChats().stream().map(Chat::getTgChatId) : Stream.empty())
            .collect(Collectors.toList());
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
