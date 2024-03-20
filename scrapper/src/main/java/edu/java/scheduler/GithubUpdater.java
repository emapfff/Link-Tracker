package edu.java.scheduler;

import edu.java.clients.GitHubClient;
import edu.java.domain.dto.LinkDto;
import edu.java.responses.RepositoryResponse;
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

    @Override
    public int update(LinkDto linkDto) {
        URI link = linkDto.getUrl();
        String[] pathSegments = link.getPath().split("/");
        String user = pathSegments[1];
        String repo = pathSegments[2];
        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository(user, repo).block();
        if (repositoryResponse == null) {
            log.info("response is null");
            throw new RuntimeException();
        }
        OffsetDateTime lastUpdate = repositoryResponse.lastUpdate();
        if (lastUpdate.isAfter(linkDto.getLastUpdate())) {
            return 1;
        }
        return 0;
    }
}
