package edu.java.domain;

import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface StackOverflowLinkRepository {
    void add(Long tgChatId, URI url, Integer answerCount);

    List<StackOverflowDto> findAllByTgChatIdAndUrl(Long tgChatId, @NotNull URI url);

    StackOverflowDto findStackOverflowLinkByLinkId(Long linkId);

    List<StackOverflowDto> findAll();

    void setAnswersCount(LinkDto link, Integer answerCount);
}
