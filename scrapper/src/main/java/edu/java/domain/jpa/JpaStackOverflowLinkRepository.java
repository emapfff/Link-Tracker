package edu.java.domain.jpa;

import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import edu.java.domain.entity.Chat;
import edu.java.domain.entity.Link;
import edu.java.domain.entity.StackOverflowLink;
import edu.java.domain.jpa.bases.BaseJpaChatRepository;
import edu.java.domain.jpa.bases.BaseJpaLinkRepository;
import edu.java.domain.jpa.bases.BaseJpaStackOverflowLinkRepository;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class JpaStackOverflowLinkRepository implements StackOverflowLinkRepository {
    private final BaseJpaStackOverflowLinkRepository baseJpaStackOverflowLinkRepository;

    private final BaseJpaChatRepository baseJpaChatRepository;

    private final BaseJpaLinkRepository baseJpaLinkRepository;

    @Override
    public void add(Long tgChatId, URI url, Integer answerCount) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        Link link = baseJpaLinkRepository.findLinkByChatsAndUrl(chat, url.toString());
        StackOverflowLink stackOverflowLink = new StackOverflowLink(link, answerCount);
        baseJpaStackOverflowLinkRepository.save(stackOverflowLink);
    }

    @Override
    public StackOverflowDto findStackOverflowLinkByTgChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        try {
            Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
            Link link = baseJpaLinkRepository.findLinkByChatsAndUrl(chat, url.toString());
            StackOverflowLink stackOverflowLink = baseJpaStackOverflowLinkRepository.findStackOverflowLinkByLink(link);
            return convertToStackOverflowDto(stackOverflowLink);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public StackOverflowDto findStackOverflowLinkByLinkId(Long linkId) {
        StackOverflowLink stackOverflowLink = baseJpaStackOverflowLinkRepository.findStackOverflowLinkByLinkId(linkId);
        return convertToStackOverflowDto(stackOverflowLink);
    }

    @Override
    public List<StackOverflowDto> findAll() {
        return baseJpaStackOverflowLinkRepository.findAll().stream()
            .map(this::convertToStackOverflowDto)
            .collect(Collectors.toList());
    }

    @Override
    public void setAnswersCount(LinkDto link, Integer answerCount) {
        StackOverflowLink stackOverflowLink =
            baseJpaStackOverflowLinkRepository.findStackOverflowLinkByLinkId(link.id());
        stackOverflowLink.setAnswerCount(answerCount);
    }

    @Contract("_->new")
    private StackOverflowDto convertToStackOverflowDto(StackOverflowLink stackOverflowLink) {
        return new StackOverflowDto(
            stackOverflowLink.getId(),
            stackOverflowLink.getLink().getId(),
            stackOverflowLink.getAnswerCount()
        );
    }
}
