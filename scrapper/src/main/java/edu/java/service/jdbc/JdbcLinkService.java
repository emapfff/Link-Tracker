package edu.java.service.jdbc;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.domain.jdbc.JdbcGithubLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcStackOverflowLinkRepository;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.responses.BranchResponse;
import edu.java.responses.QuestionResponse;
import edu.java.responses.RepositoryResponse;
import edu.java.service.LinkService;
import edu.java.tools.LinkParse;
import edu.java.tools.Urls;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class JdbcLinkService implements LinkService {
    private final static String CHAT_NOT_FOUND = "Чат не был добавлен";
    private final static String LINK_NOT_FOUND = "Ссылка не найдена";
    private final static String INCORRECT_LINK = "Неверна указана ссылка";
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcChatRepository jdbcChatRepository;
    private final LinkParse linkParse;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final JdbcGithubLinkRepository githubLinkRepository;
    private final JdbcStackOverflowLinkRepository stackOverflowLinkRepository;

    @Override
    @Transactional
    public LinkDto add(Long tgChatId, URI url) {
        if (jdbcChatRepository.existIdByTgChatId(tgChatId) == 0) {
            throw new IncorrectParametersException(CHAT_NOT_FOUND);
        }
        Urls typeOfLink = linkParse.parse(url);
        switch (typeOfLink) {
            case GITHUB -> {
                RepositoryResponse repResponse = gitHubClient.fetchRepository(
                    linkParse.getGithubUser(url),
                    linkParse.getGithubRepo(url)
                ).block();
                List<BranchResponse> branchResponse = gitHubClient.fetchBranch(
                    linkParse.getGithubUser(url),
                    linkParse.getGithubRepo(url)
                ).block();
                jdbcLinkRepository.add(tgChatId, url, repResponse.lastUpdate());
                githubLinkRepository.add(tgChatId, url, branchResponse.size());
            }
            case STACKOVERFLOW -> {
                QuestionResponse question =
                    stackOverflowClient.fetchQuestion(linkParse.getStackOverFlowId(url)).block();
                jdbcLinkRepository.add(tgChatId, url, question.items().getLast().lastActivity());
                stackOverflowLinkRepository.add(tgChatId, url, question.items().getLast().answerCount());
            }
            default -> throw new IncorrectParametersException(INCORRECT_LINK);
        }
        return jdbcLinkRepository.findAllByUrl(url).getLast();
    }

    @Override
    @Transactional
    public LinkDto remove(Long tgChatId, URI url) {
        if (jdbcChatRepository.existIdByTgChatId(tgChatId) == 0) {
            throw new IncorrectParametersException(CHAT_NOT_FOUND);
        }
        if (jdbcLinkRepository.existLinkByUriAndTgChatId(tgChatId, url) == 0) {
            throw new IncorrectParametersException(LINK_NOT_FOUND);
        } else {
            LinkDto removingLink = jdbcLinkRepository.findLinkByChatIdAndUrl(tgChatId, url);
            jdbcLinkRepository.remove(tgChatId, url);
            return removingLink;
        }
    }

    @Transactional
    @Override
    public Collection<LinkDto> listAll(Long tgChatId) {
        if (jdbcChatRepository.existIdByTgChatId(tgChatId) == 0) {
            throw new IncorrectParametersException(CHAT_NOT_FOUND);
        }
        return jdbcLinkRepository.findAllByTgChatId(tgChatId);
    }
}
