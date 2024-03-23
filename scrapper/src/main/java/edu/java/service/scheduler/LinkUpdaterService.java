package edu.java.service.scheduler;

import dto.LinkUpdateRequest;
import edu.java.clients.BotClient;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.repository.JdbcLinkRepository;
import edu.java.tools.LinkParse;
import edu.java.tools.Urls;
import edu.java.updaters.GithubUpdater;
import edu.java.updaters.StackOverFlowUpdater;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LinkUpdaterService {
    @Autowired
    private BotClient botClient;

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Autowired
    private GithubUpdater githubUpdater;

    @Autowired
    private StackOverFlowUpdater stackOverFlowUpdater;

    @Autowired
    private LinkParse linkParse;

    public void checkUpdates() {
        List<LinkDto> links = jdbcLinkRepository.findAll();
        log.info("Получил ссылки из бд");
        for (var link : links) {
            if (linkParse.parse(link.getUrl()).equals(Urls.GITHUB)) {
                if (githubUpdater.update(link) == 1) {
                    botClient.sendUpdate(new LinkUpdateRequest(
                        link.getId(),
                        link.getUrl(),
                        "Пришло обновление с github!",
                        jdbcLinkRepository.findAllTgChatIdsByUrl(link.getUrl())
                    ));
                }
            } else {
                if (stackOverFlowUpdater.update(link) == 1) {
                    botClient.sendUpdate(new LinkUpdateRequest(
                        link.getId(),
                        link.getUrl(),
                        "Пришло уведомление со stackoverflow!",
                        jdbcLinkRepository.findAllTgChatIdsByUrl(link.getUrl())
                    ));
                }
            }
        }
    }

}
