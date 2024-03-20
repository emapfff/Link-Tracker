package edu.java.service.scheduler;

import dto.LinkUpdateRequest;
import edu.java.clients.BotClient;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.repository.JdbcLinkRepository;
import edu.java.scheduler.GithubUpdater;
import edu.java.scheduler.StackOverFlowUpdater;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LinkUpdaterService {
    private static final String DESCRIPTION = "Обновлено";
    @Autowired
    private BotClient botClient;

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Autowired
    private GithubUpdater githubUpdater;

    @Autowired
    private StackOverFlowUpdater stackOverFlowUpdater;

    public void checkUpdates() {
        List<LinkDto> links = jdbcLinkRepository.findAll();
        for (var link : links) {
            if ((link.getUrl().getHost()).equals("github.com")) {
                if (githubUpdater.update(link) == 1) {
                    botClient.sendUpdate(new LinkUpdateRequest(
                        link.getId(),
                        link.getUrl(),
                        DESCRIPTION,
                        jdbcLinkRepository.findAllTgChatIdsByUrl(link.getUrl())
                    ));
                }
            } else if ((link.getUrl().getHost()).equals("stackoverflow.com")) {
                if (stackOverFlowUpdater.update(link) == 1) {
                    botClient.sendUpdate(new LinkUpdateRequest(
                        link.getId(),
                        link.getUrl(),
                        DESCRIPTION,
                        jdbcLinkRepository.findAllTgChatIdsByUrl(link.getUrl())
                    ));
                }
            }
        }
    }

}
