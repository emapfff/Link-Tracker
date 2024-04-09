package edu.java.service;

import edu.java.clients.GitHubClient;
import edu.java.domain.GithubLinkRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import edu.java.response.BranchResponse;
import edu.java.response.RepositoryResponse;
import edu.java.tool.LinkParser;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GithubUpdater {
    private final GitHubClient githubClient;
    private final LinkParser linkParse;
    private final GithubLinkRepository githubLinkRepository;
    private final LinkRepository linkRepository;

    @Transactional
    public boolean update(@NotNull LinkDto linkDto) {
        URI link = linkDto.url();
        String user = linkParse.getGithubUser(link);
        String repo = linkParse.getGithubRepo(link);
        RepositoryResponse repositoryResponse = githubClient.fetchRepository(user, repo).block();
        OffsetDateTime lastUpdate = repositoryResponse.lastUpdate();
        if (lastUpdate.isAfter(linkDto.lastUpdate())) {
            linkRepository.setLastUpdate(linkDto, lastUpdate);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean checkBranches(@NotNull LinkDto linkDto) {
        GithubLinkDto githubLinkDto = githubLinkRepository.findGithubLinkByLinkId(linkDto.id());
        URI link = linkDto.url();
        String user = linkParse.getGithubUser(link);
        String repo = linkParse.getGithubRepo(link);
        List<BranchResponse> branches = githubClient.fetchBranch(user, repo).block();
        int bdCountBranches = githubLinkDto.countBranches();
        int newCountBranches = branches.size();
        if (newCountBranches > bdCountBranches) {
            githubLinkRepository.setCountBranches(linkDto, newCountBranches);
            return true;
        }
        return false;
    }
}
