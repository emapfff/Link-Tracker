package edu.java.updaters;

import edu.java.clients.StackOverflowClient;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcStackOverflowLinkRepository;
import edu.java.responses.QuestionResponse;
import edu.java.tools.LinkParse;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class StackOverFlowUpdater implements LinkUpdater {
    private StackOverflowClient stackOverflowClient;
    private JdbcStackOverflowLinkRepository stackOverflowLinkRepository;
    private LinkParse linkParse;
    private JdbcLinkRepository linkRepository;

    @Override
    public boolean update(LinkDto linkDto) {
        URI link = linkDto.url();
        Long id = linkParse.getStackOverFlowId(link);
        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(id).block();
        if (questionResponse.items().getLast().lastActivity().isAfter(linkDto.lastUpdate())) {
            linkRepository.setLastUpdate(linkDto, questionResponse.items().getLast().lastActivity());
            return true;
        }
        return false;
    }

    public boolean checkAnswers(LinkDto linkDto) {
        URI link = linkDto.url();
        Long id = linkParse.getStackOverFlowId(link);
        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(id).block();
        StackOverflowDto stackOverflowDto = stackOverflowLinkRepository.findStackOverflowLinkByLinkId(linkDto.id());
        Integer oldAnswerCount = stackOverflowDto.answerCount();
        Integer newAnswerCount = questionResponse.items().getLast().answerCount();
        if (newAnswerCount > oldAnswerCount) {
            stackOverflowLinkRepository.setAnswersCount(linkDto, newAnswerCount);
            return true;
        }
        return false;
    }
}
