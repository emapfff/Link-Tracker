package edu.java.service;

import dto.LinkUpdateRequest;
import edu.java.clients.BotClient;
import edu.java.domain.LinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.tool.LinkParser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkUpdaterService {
    private final BotClient botClient;
    private final LinkRepository linkRepository;
    private final GithubUpdater githubUpdater;
    private final StackOverflowUpdater stackOverflowUpdater;
    private final LinkParser linkParse;

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
