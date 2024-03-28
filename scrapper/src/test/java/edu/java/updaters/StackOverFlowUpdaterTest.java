package edu.java.updaters;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.configuration.DataSourceConfig;
import edu.java.configuration.JdbcTemplateConfig;
import edu.java.configuration.TransactionManagerConfig;
import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import edu.java.domain.dto.StackOverflowDto;
import edu.java.domain.jdbc.JdbcConsistsRepository;
import edu.java.domain.jdbc.JdbcGithubLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcStackOverflowLinkRepository;
import edu.java.responses.QuestionResponse;
import edu.java.responses.QuestionResponse.ItemResponse;
import edu.java.scrapper.IntegrationTest;
import edu.java.tools.LinkParse;
import io.restassured.module.spring.commons.config.ClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {JdbcLinkRepository.class, JdbcConsistsRepository.class, StackOverflowClient.class,
     StackOverFlowUpdater.class, LinkParse.class, JdbcStackOverflowLinkRepository.class})
@ContextConfiguration(classes = {JdbcTemplateConfig.class, DataSourceConfig.class, TransactionManagerConfig.class,
    ClientConfig.class})
@Transactional
@Rollback
class StackOverFlowUpdaterTest extends IntegrationTest {
    private final LinkDto link = new LinkDto(
        1L,
        URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
        OffsetDateTime.now()
    );

    List<ItemResponse> itemsMinusDay = List.of(
        new ItemResponse(false, 20, 1642028L, OffsetDateTime.now().minusDays(1)));

    List<ItemResponse> itemsPlusDay = List.of(
        new ItemResponse(false, 20, 1642028L, OffsetDateTime.now().plusDays(1)));


    @Autowired
    private StackOverFlowUpdater stackOverFlowUpdater;

    @MockBean
    private StackOverflowClient stackOverflowClient;

    @MockBean
    private JdbcLinkRepository linkRepository;
    @MockBean
    private JdbcStackOverflowLinkRepository stackOverflowLinkRepository;

    @Test
    void updateNeededTest() {
        when(stackOverflowClient.fetchQuestion(anyLong())).thenReturn(Mono.just(new QuestionResponse(itemsPlusDay)));
        doNothing().when(linkRepository).setLastUpdate(any(), any());

        boolean result = stackOverFlowUpdater.update(link);

        assertTrue(result);
    }


    @Test
    void updateNoNeededTest() {
        when(stackOverflowClient.fetchQuestion(anyLong())).thenReturn(Mono.just(new QuestionResponse(itemsMinusDay)));
        boolean result = stackOverFlowUpdater.update(link);

        assertFalse(result);
    }
    @Test
    void checkAnswersTrue() {
        StackOverflowDto stackOverflowDto = new StackOverflowDto(1L, link.id(), 19);
        when(stackOverflowLinkRepository.findStackOverflowLinkByLinkId(anyLong())).thenReturn(stackOverflowDto);
        when(stackOverflowClient.fetchQuestion(anyLong())).thenReturn(Mono.just(new QuestionResponse(itemsMinusDay)));
        doNothing().when(stackOverflowLinkRepository).setAnswersCount(any(), any());

        boolean result = stackOverFlowUpdater.checkAnswers(link);

        assertTrue(result);
    }

    @Test
    void checkAnswersFalse() {
        StackOverflowDto stackOverflowDto = new StackOverflowDto(1L, link.id(), 20);
        when(stackOverflowLinkRepository.findStackOverflowLinkByLinkId(anyLong())).thenReturn(stackOverflowDto);
        when(stackOverflowClient.fetchQuestion(anyLong())).thenReturn(Mono.just(new QuestionResponse(itemsPlusDay)));

        boolean result = stackOverFlowUpdater.checkAnswers(link);

        assertFalse(result);
    }

}
