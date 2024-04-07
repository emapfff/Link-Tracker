package edu.java.updaters;

import edu.java.clients.StackOverflowClient;
import edu.java.domain.LinkRepository;
import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import edu.java.responses.QuestionResponse;
import edu.java.responses.QuestionResponse.ItemResponse;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.StackOverflowUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import edu.java.tools.LinkParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StackOverflowUpdaterTest extends IntegrationTest{
    private final LinkDto link = new LinkDto(
        1L,
        URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
        OffsetDateTime.now()
    );

    List<ItemResponse> itemsMinusDay = List.of(
        new ItemResponse(false, 20, 1642028L, OffsetDateTime.now().minusDays(1)));

    List<ItemResponse> itemsPlusDay = List.of(
        new ItemResponse(false, 20, 1642028L, OffsetDateTime.now().plusDays(1)));

    @Mock
    private StackOverflowClient stackOverflowClient;

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private LinkParser linkParse;

    @Mock
    private StackOverflowLinkRepository stackOverflowLinkRepository;

    @InjectMocks
    private StackOverflowUpdater stackOverflowUpdater;

    @Test
    void updateNeededTest() {
        when(stackOverflowClient.fetchQuestion(anyLong())).thenReturn(Mono.just(new QuestionResponse(itemsPlusDay)));
        doNothing().when(linkRepository).setLastUpdate(any(), any());
        when(linkParse.getStackOverFlowId(link.url())).thenReturn(1642028L);

        boolean result = stackOverflowUpdater.update(link);
        assertTrue(result);
    }


    @Test
    void updateNoNeededTest() {
        when(stackOverflowClient.fetchQuestion(anyLong())).thenReturn(Mono.just(new QuestionResponse(itemsMinusDay)));
        when(linkParse.getStackOverFlowId(link.url())).thenReturn(1642028L);

        boolean result = stackOverflowUpdater.update(link);

        assertFalse(result);
    }
    @Test
    void checkAnswersTrue() {
        StackOverflowDto stackOverflowDto = new StackOverflowDto(1L, link.id(), 19);
        when(stackOverflowLinkRepository.findStackOverflowLinkByLinkId(anyLong())).thenReturn(stackOverflowDto);
        when(stackOverflowClient.fetchQuestion(anyLong())).thenReturn(Mono.just(new QuestionResponse(itemsMinusDay)));
        when(linkParse.getStackOverFlowId(link.url())).thenReturn(1642028L);
        doNothing().when(stackOverflowLinkRepository).setAnswersCount(any(), any());

        boolean result = stackOverflowUpdater.checkAnswers(link);

        assertTrue(result);
    }

    @Test
    void checkAnswersFalse() {
        StackOverflowDto stackOverflowDto = new StackOverflowDto(1L, link.id(), 20);
        when(stackOverflowLinkRepository.findStackOverflowLinkByLinkId(anyLong())).thenReturn(stackOverflowDto);
        when(linkParse.getStackOverFlowId(link.url())).thenReturn(1642028L);
        when(stackOverflowClient.fetchQuestion(anyLong())).thenReturn(Mono.just(new QuestionResponse(itemsPlusDay)));

        boolean result = stackOverflowUpdater.checkAnswers(link);

        assertFalse(result);
    }

}
