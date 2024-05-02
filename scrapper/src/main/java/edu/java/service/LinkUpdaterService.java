package edu.java.service;

import edu.java.domain.LinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.dto.LinkUpdateRequest;
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
    private final NotificationSender notificationSender;
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
                        LinkUpdateRequest updatedRepo = new LinkUpdateRequest(
                            link.id(),
                            link.url(),
                            "Пришло обновление с github!",
                            linkRepository.findAllTgChatIdsByUrl(link.url())
                        );
                        notificationSender.send(updatedRepo);
                    }
                    switch (githubUpdater.checkBranches(link)) {
                        case ADD -> {
                            LinkUpdateRequest newBranch = new LinkUpdateRequest(
                                link.id(),
                                link.url(),
                                "Добавлена новая ветка!",
                                linkRepository.findAllTgChatIdsByUrl(link.url())
                            );
                            log.info(newBranch.description());
                            notificationSender.send(newBranch);
                        }
                        case DELETE -> {
                            LinkUpdateRequest deleteBranch = new LinkUpdateRequest(
                                link.id(),
                                link.url(),
                                "Удалена ветка!",
                                linkRepository.findAllTgChatIdsByUrl(link.url())
                            );
                            log.info(deleteBranch.description());
                            notificationSender.send(deleteBranch);
                        }
                        default -> log.info("Нет изменений в ветках");
                    }
                }
                case STACKOVERFLOW -> {
                    if (stackOverflowUpdater.update(link)) {
                        LinkUpdateRequest newNotification = new LinkUpdateRequest(
                            link.id(),
                            link.url(),
                            "Пришло уведомление со stackoverflow!",
                            linkRepository.findAllTgChatIdsByUrl(link.url())
                        );
                        log.info(newNotification.description());
                        notificationSender.send(newNotification);
                    }
                    switch (stackOverflowUpdater.checkAnswers(link)) {
                        case ADD -> {
                            LinkUpdateRequest newAnswer = new LinkUpdateRequest(
                                link.id(),
                                link.url(),
                                "Пришел новый ответ!",
                                linkRepository.findAllTgChatIdsByUrl(link.url())
                            );
                            log.info(newAnswer.description());
                            notificationSender.send(newAnswer);
                        }
                        case DELETE -> {
                            LinkUpdateRequest deleteAnswer = new LinkUpdateRequest(
                                link.id(),
                                link.url(),
                                "Ответ был удален!",
                                linkRepository.findAllTgChatIdsByUrl(link.url())
                            );
                            log.info(deleteAnswer.description());
                            notificationSender.send(deleteAnswer);
                        }
                        default -> log.info("Нет изменений в вопросе");
                    }
                }
                default -> throw new IncorrectParametersException("Неверная ссылка");
            }
        }
    }

}
