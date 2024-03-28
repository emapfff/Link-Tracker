package edu.java.service.scheduler;

import dto.LinkUpdateRequest;
import edu.java.clients.BotClient;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.tools.LinkParse;
import edu.java.updaters.GithubUpdater;
import edu.java.updaters.StackOverFlowUpdater;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void checkUpdates() {
        List<LinkDto> links = jdbcLinkRepository.findAll();
        log.info("Получил ссылки из бд");
        for (var link : links) {
            switch (linkParse.parse(link.url())) {
                case GITHUB -> {
                    if (githubUpdater.update(link)) {
                        botClient.sendUpdate(new LinkUpdateRequest(
                            link.id(),
                            link.url(),
                            "Пришло обновление с github!",
                            jdbcLinkRepository.findAllTgChatIdsByUrl(link.url())
                        ));
                    }
                    if (githubUpdater.checkBranches(link)) {
                        botClient.sendUpdate(new LinkUpdateRequest(
                            link.id(),
                            link.url(),
                            "Добавлена новая ветка!",
                            jdbcLinkRepository.findAllTgChatIdsByUrl(link.url())
                        ));
                    }
                }
                case STACKOVERFLOW -> {
                    if (stackOverFlowUpdater.update(link)) {
                        botClient.sendUpdate(new LinkUpdateRequest(
                            link.id(),
                            link.url(),
                            "Пришло уведомление со stackoverflow!",
                            jdbcLinkRepository.findAllTgChatIdsByUrl(link.url())
                        ));
                    }
                    if (stackOverFlowUpdater.checkAnswers(link)) {
                        botClient.sendUpdate(new LinkUpdateRequest(
                            link.id(),
                            link.url(),
                            "Пришел новый ответ!",
                            jdbcLinkRepository.findAllTgChatIdsByUrl(link.url())
                        ));
                    }
                }
                default -> throw new IncorrectParametersException("Неверная ссылка");
            }
        }
    }

}
