package edu.java.domain;

import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GithubLinkRepository {
    void add(Long tgChatId, URI url, Integer countBranches);

    GithubLinkDto findGithubLinkByTgChatIdAndUrl(Long tgChatId, @NotNull URI url);

    GithubLinkDto findGithubLinkByLinkId(Long linkId);

    List<GithubLinkDto> findAll();

    void setCountBranches(@NotNull LinkDto link, Integer countBranches);
}
