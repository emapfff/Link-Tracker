package edu.java.updaters;

import edu.java.clients.GitHubClient;
import edu.java.domain.dto.LinkDto;
import edu.java.responses.RepositoryResponse;
import edu.java.tools.LinkParse;
import java.net.URI;
import java.time.OffsetDateTime;
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

    @Override
    public int update(LinkDto linkDto) {
        URI link = linkDto.getUrl();
        String user = linkParse.getGithubUser(link);
        String repo = linkParse.getGithubRepo(link);
        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository(user, repo).block();
        if (repositoryResponse == null) {
            log.info("response is null");
            throw new RuntimeException();
        }
        log.info("Получил инфу с api");
        OffsetDateTime lastUpdate = repositoryResponse.lastUpdate();
        if (lastUpdate.isAfter(linkDto.getLastUpdate())) {
            log.info("Нужно обновить");
            return 1;
        }
        log.info("Не требует обновлений");
        return 0;
    }
}
