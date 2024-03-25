package edu.java.updaters;

import edu.java.clients.StackOverflowClient;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcStackOverflowLinkRepository;
import edu.java.responses.QuestionResponse;
import edu.java.tools.LinkParse;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StackOverFlowUpdater implements LinkUpdater {
    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Autowired
    private JdbcStackOverflowLinkRepository stackOverflowLinkRepository;

    @Autowired
    private LinkParse linkParse;

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Override
    public int update(LinkDto linkDto) {
        URI link = linkDto.getUrl();
        Long id = linkParse.getStackOverFlowId(link);
        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(id).block();
        if (questionResponse.items().getLast().lastActivity().isAfter(linkDto.getLastUpdate())) {
            linkRepository.setLastUpdate(linkDto, questionResponse.items().getLast().lastActivity());
            return 1;
        }
        return 0;
    }

    public int checkAnswers(LinkDto linkDto) {
        URI link = linkDto.getUrl();
        Long id = linkParse.getStackOverFlowId(link);
        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(id).block();
        StackOverflowDto stackOverflowDto = stackOverflowLinkRepository.findStackOverflowLinkByLinkId(linkDto.getId());
        Integer oldAnswerCount = stackOverflowDto.getAnswerCount();
        Integer newAnswerCount = questionResponse.items().getLast().answerCount();
        if (newAnswerCount > oldAnswerCount) {
            stackOverflowLinkRepository.setAnswersCount(linkDto, newAnswerCount);
            return 1;
        }
        return 0;
    }
}
