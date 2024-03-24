package edu.java.updaters;

import edu.java.clients.StackOverflowClient;
import edu.java.domain.dto.LinkDto;
import edu.java.responses.QuestionResponse;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StackOverFlowUpdater implements LinkUpdater {
    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Override
    public int update(LinkDto linkDto) {
        URI link = linkDto.getUrl();
        String[] pathSegments = link.getPath().split("/");
        String numOfQuestion = pathSegments[2];
        long id = Long.parseLong(numOfQuestion);
        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(id).block();
        if (questionResponse.items().getLast().lastActivity().isAfter(linkDto.getLastUpdate())) {
            return 1;
        }
        return 0;
    }
}
