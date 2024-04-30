package edu.java.service;

import edu.java.clients.StackOverflowClient;
import edu.java.domain.LinkRepository;
import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import edu.java.response.QuestionResponse;
import edu.java.tool.LinkParser;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StackOverflowUpdater {
    private final StackOverflowClient stackOverflowClient;
    private final StackOverflowLinkRepository stackOverflowLinkRepository;
    private final LinkParser linkParse;
    private final LinkRepository linkRepository;

    @Transactional
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

    @Transactional
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
