package edu.java.service;

import dto.LinkUpdateRequest;
import edu.java.clients.BotClient;
import edu.java.domain.LinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.tools.LinkParse;
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
    private LinkRepository linkRepository;
    @Autowired
    private GithubUpdater githubUpdater;
    @Autowired
    private StackOverflowUpdater stackOverflowUpdater;
    @Autowired
    private LinkParse linkParse;

    @Transactional
    public void checkUpdates() {
        List<LinkDto> links = linkRepository.findAll();
        for (var link : links) {
            switch (linkParse.parse(link.url())) {
                case GITHUB -> {
                    if (githubUpdater.update(link)) {
                        log.info("Требуются обновления для гитхаба");
                        botClient.sendUpdate(new LinkUpdateRequest(
                            link.id(),
                            link.url(),
                            "Пришло обновление с github!",
                            linkRepository.findAllTgChatIdsByUrl(link.url())
                        ));
                    }
                    if (githubUpdater.checkBranches(link)) {
                        log.info("Требуются обновления для гитхаба по веткам");
                        botClient.sendUpdate(new LinkUpdateRequest(
                            link.id(),
                            link.url(),
                            "Добавлена новая ветка!",
                            linkRepository.findAllTgChatIdsByUrl(link.url())
                        ));
                    }
                    log.info("Обновления не требуются для гитхаба");
                }
                case STACKOVERFLOW -> {
                    if (stackOverflowUpdater.update(link)) {
                        log.info("Требуются обновления для стака");
                        botClient.sendUpdate(new LinkUpdateRequest(
                            link.id(),
                            link.url(),
                            "Пришло уведомление со stackoverflow!",
                            linkRepository.findAllTgChatIdsByUrl(link.url())
                        ));
                    }
                    if (stackOverflowUpdater.checkAnswers(link)) {
                        log.info("Требуются обновления для стака по ответам");
                        botClient.sendUpdate(new LinkUpdateRequest(
                            link.id(),
                            link.url(),
                            "Пришел новый ответ!",
                            linkRepository.findAllTgChatIdsByUrl(link.url())
                        ));
                    }
                    log.info("Обновления не требуются для стака");

                }
                default -> throw new IncorrectParametersException("Неверная ссылка");
            }
        }
    }

}
