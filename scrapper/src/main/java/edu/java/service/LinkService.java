package edu.java.service;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.domain.ChatRepository;
import edu.java.domain.GithubLinkRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.responses.BranchResponse;
import edu.java.responses.QuestionResponse;
import edu.java.responses.RepositoryResponse;
import edu.java.tools.LinkParse;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkService {
    private final static String CHAT_NOT_FOUND = "Чат не был зарегистрирован. Для регистрации введите команду /start";
    private final static String LINK_NOT_FOUND = "Ссылка не найдена";
    private final static String INCORRECT_LINK = "Неверна указана ссылка";
    private final static String LINK_EXIST = "Ссылка уже была добавлена";
    @Autowired
    private LinkParse linkParse;
    @Autowired
    private GitHubClient gitHubClient;
    @Autowired
    private StackOverflowClient stackOverflowClient;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private GithubLinkRepository githubLinkRepository;
    @Autowired
    private StackOverflowLinkRepository stackOverflowLinkRepository;

    @Transactional
    public LinkDto add(Long tgChatId, URI url) {
        if (chatRepository.existIdByTgChatId(tgChatId) == 0) {
            throw new IncorrectParametersException(CHAT_NOT_FOUND);
        }
        switch (linkParse.parse(url)) {
            case GITHUB -> {
                if (linkRepository.existLinkByUriAndTgChatId(tgChatId, url) != 0) {
                    throw new IncorrectParametersException(LINK_EXIST);
                }
                String githubUser = linkParse.getGithubUser(url);
                String githubRepo = linkParse.getGithubRepo(url);
                RepositoryResponse repositoryResponse = gitHubClient.fetchRepository(
                    githubUser,
                    githubRepo
                ).block();
                List<BranchResponse> branchResponse = gitHubClient.fetchBranch(
                    githubUser,
                    githubRepo
                ).block();
                linkRepository.add(tgChatId, url, repositoryResponse.lastUpdate());
                githubLinkRepository.add(tgChatId, url, branchResponse.size());
            }
            case STACKOVERFLOW -> {
                Long questionId = linkParse.getStackOverFlowId(url);
                QuestionResponse question = stackOverflowClient.fetchQuestion(questionId).block();
                linkRepository.add(tgChatId, url, question.items().getLast().lastActivity());
                stackOverflowLinkRepository.add(tgChatId, url, question.items().getLast().answerCount());
            }
            default -> throw new IncorrectParametersException(INCORRECT_LINK);
        }
        return linkRepository.findAllByUrl(url).getLast();
    }

    @Transactional
    public LinkDto remove(Long tgChatId, URI url) {
        if (chatRepository.existIdByTgChatId(tgChatId) == 0) {
            throw new IncorrectParametersException(CHAT_NOT_FOUND);
        }
        if (linkRepository.existLinkByUriAndTgChatId(tgChatId, url) == 0) {
            throw new LinkNotFoundException(LINK_NOT_FOUND);
        } else {
            LinkDto removingLink = linkRepository.findLinkByChatIdAndUrl(tgChatId, url);
            linkRepository.remove(tgChatId, url);
            return removingLink;
        }
    }

    @Transactional
    public Collection<LinkDto> listAll(Long tgChatId) {
        if (chatRepository.existIdByTgChatId(tgChatId) == 0) {
            throw new IncorrectParametersException(CHAT_NOT_FOUND);
        }
        return linkRepository.findAllByTgChatId(tgChatId);
    }
}
