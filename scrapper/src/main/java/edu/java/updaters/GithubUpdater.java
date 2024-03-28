package edu.java.updaters;

import edu.java.clients.GitHubClient;
import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.jdbc.JdbcGithubLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.responses.BranchResponse;
import edu.java.responses.RepositoryResponse;
import edu.java.tools.LinkParse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class GithubUpdater implements LinkUpdater {
    private GitHubClient gitHubClient;
    private LinkParse linkParse;
    private JdbcGithubLinkRepository githubLinkRepository;
    private JdbcLinkRepository linkRepository;


    @Override
    public boolean update(LinkDto linkDto) {
        URI link = linkDto.url();
        String user = linkParse.getGithubUser(link);
        String repo = linkParse.getGithubRepo(link);
        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository(user, repo).block();
        log.info("Получил инфу с api");
        OffsetDateTime lastUpdate = repositoryResponse.lastUpdate();
        if (lastUpdate.isAfter(linkDto.lastUpdate())) {
            log.info("Нужно обновить");
            linkRepository.setLastUpdate(linkDto, lastUpdate);
            return true;
        }
        log.info("Не требует обновлений");
        return false;
    }

    public boolean checkBranches(LinkDto linkDto) {
        GithubLinkDto githubLinkDto = githubLinkRepository.findGithubLinkByLinkId(linkDto.id());
        URI link = linkDto.url();
        String user = linkParse.getGithubUser(link);
        String repo = linkParse.getGithubRepo(link);
        List<BranchResponse> branches = gitHubClient.fetchBranch(user, repo).block();
        log.info("Получил инфу о ветках репы");
        int bdCountBranches = githubLinkDto.countBranches();
        int newCountBranches = branches.size();
        if (newCountBranches > bdCountBranches) {
            log.info("Нужно обновить инфу о ветка репы");
            githubLinkRepository.setCountBranches(linkDto, newCountBranches);
            return true;
        }
        return false;
    }
}
