package edu.java.service;

import edu.java.clients.GitHubClient;
import edu.java.domain.GithubLinkRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import edu.java.response.ListBranchesResponse;
import edu.java.response.RepositoryResponse;
import edu.java.tool.Changes;
import edu.java.tool.LinkParser;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.tool.Changes.ADD;
import static edu.java.tool.Changes.DELETE;
import static edu.java.tool.Changes.NOTHING;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public Changes checkBranches(@NotNull LinkDto linkDto) {
        GithubLinkDto githubLinkDto = githubLinkRepository.findGithubLinkByLinkId(linkDto.id());
        URI link = linkDto.url();
        String user = linkParse.getGithubUser(link);
        String repo = linkParse.getGithubRepo(link);
        ListBranchesResponse listBranchesResponse = githubClient.fetchBranch(user, repo).block();
        int bdCountBranches = githubLinkDto.countBranches();
        int newCountBranches = listBranchesResponse.listBranches().size();
        if (newCountBranches > bdCountBranches) {
            githubLinkRepository.setCountBranches(linkDto, newCountBranches);
            return ADD;
        } else if (newCountBranches < bdCountBranches) {
            githubLinkRepository.setCountBranches(linkDto, newCountBranches);
            return DELETE;
        }
        return NOTHING;
    }
}
