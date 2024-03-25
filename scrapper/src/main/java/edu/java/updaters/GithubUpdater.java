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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GithubUpdater implements LinkUpdater {
    @Autowired
    private GitHubClient gitHubClient;

    @Autowired
    private LinkParse linkParse;

    @Autowired
    private JdbcGithubLinkRepository githubLinkRepository;

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Override
    public int update(LinkDto linkDto) {
        URI link = linkDto.getUrl();
        String user = linkParse.getGithubUser(link);
        String repo = linkParse.getGithubRepo(link);
        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository(user, repo).block();
        log.info("Получил инфу с api");
        OffsetDateTime lastUpdate = repositoryResponse.lastUpdate();
        if (lastUpdate.isAfter(linkDto.getLastUpdate())) {
            log.info("Нужно обновить");
            linkRepository.setLastUpdate(linkDto, lastUpdate);
            return 1;
        }
        log.info("Не требует обновлений");
        return 0;
    }

    public int checkBranches(LinkDto linkDto) {
        GithubLinkDto githubLinkDto = githubLinkRepository.findGithubLinkByLinkId(linkDto.getId());
        URI link = linkDto.getUrl();
        String user = linkParse.getGithubUser(link);
        String repo = linkParse.getGithubRepo(link);
        List<BranchResponse> branches = gitHubClient.fetchBranch(user, repo).collectList().block();
        log.info("Получил инфу о ветках репы");
        int bdCountBranches = githubLinkDto.getCountBranches();
        int newCountBranches = branches.size();
        if (newCountBranches > bdCountBranches) {
            log.info("Нужно обновить инфу о ветка репы");
            githubLinkRepository.setCountBranches(linkDto, newCountBranches);
            return 1;
        }
        return 0;
    }
}
