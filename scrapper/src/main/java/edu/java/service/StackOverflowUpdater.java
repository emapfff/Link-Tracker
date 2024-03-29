package edu.java.service;

import edu.java.clients.StackOverflowClient;
import edu.java.domain.LinkRepository;
import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import edu.java.responses.QuestionResponse;
import edu.java.tools.LinkParse;
import java.net.URI;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StackOverflowUpdater {
    @Autowired
    private StackOverflowClient stackOverflowClient;
    @Autowired
    private StackOverflowLinkRepository stackOverflowLinkRepository;
    @Autowired
    private LinkParse linkParse;
    @Autowired
    private LinkRepository linkRepository;

    public boolean update(@NotNull LinkDto linkDto) {
        URI link = linkDto.url();
        Long id = linkParse.getStackOverFlowId(link);
        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(id).block();
        if (questionResponse.items().getLast().lastActivity().isAfter(linkDto.lastUpdate())) {
            linkRepository.setLastUpdate(linkDto, questionResponse.items().getLast().lastActivity());
            return true;
        }
        return false;
    }

    public boolean checkAnswers(@NotNull LinkDto linkDto) {
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
