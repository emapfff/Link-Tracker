package edu.java.domain.jpa;

import edu.java.domain.GithubLinkRepository;
import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.entity.Chat;
import edu.java.domain.entity.GithubLink;
import edu.java.domain.entity.Link;
import edu.java.domain.jpa.bases.BaseJpaChatRepository;
import edu.java.domain.jpa.bases.BaseJpaGithubLinkRepository;
import edu.java.domain.jpa.bases.BaseJpaLinkRepository;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class JpaGithubLinkRepository implements GithubLinkRepository {
    private final BaseJpaGithubLinkRepository baseJpaGithubLinkRepository;

    private final BaseJpaLinkRepository baseJpaLinkRepository;

    private final BaseJpaChatRepository baseJpaChatRepository;

    @Override
    public void add(Long tgChatId, URI url, Integer countBranches) {
        Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
        Link link = baseJpaLinkRepository.findLinkByChatsAndUrl(chat, url.toString());
        GithubLink githubLink = new GithubLink(link, countBranches);
        baseJpaGithubLinkRepository.save(githubLink);
    }

    @Override
    public GithubLinkDto findGithubLinkByTgChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        try {
            Chat chat = baseJpaChatRepository.findChatByTgChatId(tgChatId);
            Link link = baseJpaLinkRepository.findLinkByChatsAndUrl(chat, url.toString());
            GithubLink githubLink = baseJpaGithubLinkRepository.findGithubLinkByLink(link);
            return convertToGithubDto(githubLink);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public GithubLinkDto findGithubLinkByLinkId(Long linkId) {
        GithubLink githubLink = baseJpaGithubLinkRepository.findGithubLinkByLinkId(linkId);
        return convertToGithubDto(githubLink);
    }

    @Override
    public List<GithubLinkDto> findAll() {
        return baseJpaGithubLinkRepository.findAll().stream()
            .map(this::convertToGithubDto)
            .collect(Collectors.toList());
    }

    @Override
    public void setCountBranches(@NotNull LinkDto link, Integer countBranches) {
        GithubLink githubLink = baseJpaGithubLinkRepository.findGithubLinkByLinkId(link.id());
        githubLink.setCountBranches(countBranches);
    }

    public List<GithubLink> getGithubLinks() {
        return baseJpaGithubLinkRepository.findAll();
    }

    @Contract("_->new")
    private GithubLinkDto convertToGithubDto(GithubLink githubLink) {
        return new GithubLinkDto(
            githubLink.getId(),
            githubLink.getLink().getId(),
            githubLink.getCountBranches()
        );
    }
}
