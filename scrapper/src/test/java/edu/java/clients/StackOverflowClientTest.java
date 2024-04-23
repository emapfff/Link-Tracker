package edu.java.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.backoff.ConstantBackOff;
import edu.java.backoff.ExponentialBackOff;
import edu.java.backoff.LinearBackOff;
import edu.java.configuration.ClientConfig;
import edu.java.configuration.RetryBuilder;
import edu.java.configuration.RetryPolicy;
import edu.java.response.QuestionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.resetAllScenarios;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ExponentialBackOff.class, LinearBackOff.class, ConstantBackOff.class, RetryBuilder.class})
@WireMockTest(httpPort = 8080)
class StackOverflowClientTest {
    @Autowired
    RetryBuilder retryBuilder;
    @Mock
    private ClientConfig clientConfig;

    private Retry retry;

    @InjectMocks
    private StackOverflowClient stackOverflowClient;

    @BeforeEach
    void setUp() {
        stubFor(get(urlEqualTo("/questions/123?site=stackoverflow")).inScenario("Check retry for stack")
            .whenScenarioStateIs(STARTED)
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("2")
        );
        stubFor(get(urlEqualTo("/questions/123?site=stackoverflow")).inScenario("Check retry for stack")
            .whenScenarioStateIs("2")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("3")
        );
        stubFor(get(urlEqualTo("/questions/123?site=stackoverflow")).inScenario("Check retry for stack")
            .whenScenarioStateIs("3")
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-type", "application/json")
                    .withBody("{ \"items\" : [ { \"is_answered\" : true," +
                        "\"answer_count\" : 23," +
                        "\"question_id\" : \"123\"," +
                        "\"last_activity_date\" : \"1709846695\"}]}"
                    )));
    }

    @AfterEach
    void tearDown() {
        resetAllScenarios();
    }

    @Test
    void fetchQuestionExponentialBlackOff() {
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.EXPONENTIAL);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Stackoverflow stackoverflow = new ClientConfig.Stackoverflow("", retryPolicy);
        when(clientConfig.stackoverflow()).thenReturn(stackoverflow);
        stackOverflowClient =
            new StackOverflowClient(WebClient.create("http://localhost:8080"), clientConfig, retryBuilder);

        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(123).block();

        assertNotNull(questionResponse);
        assertTrue(questionResponse.items().getFirst().isAnswered());
        assertEquals(questionResponse.items().getFirst().answerCount(), 23);
        assertEquals(questionResponse.items().getFirst().questionId(), 123);
        assertEquals(questionResponse.items().getFirst().lastActivity().toString(), "2024-03-07T21:24:55Z");
    }

    @Test
    void fetchQuestionLinearBlackOff() {
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.LINEAR);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Stackoverflow stackoverflow = new ClientConfig.Stackoverflow("", retryPolicy);
        when(clientConfig.stackoverflow()).thenReturn(stackoverflow);
        stackOverflowClient =
            new StackOverflowClient(WebClient.create("http://localhost:8080"), clientConfig, retryBuilder);

        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(123).block();

        assertNotNull(questionResponse);
        assertTrue(questionResponse.items().getFirst().isAnswered());
        assertEquals(questionResponse.items().getFirst().answerCount(), 23);
        assertEquals(questionResponse.items().getFirst().questionId(), 123);
        assertEquals(questionResponse.items().getFirst().lastActivity().toString(), "2024-03-07T21:24:55Z");
    }

    @Test
    void fetchQuestionConstantBlackOff() {
        RetryPolicy retryPolicy = new RetryPolicy();
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.CONSTANT);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        ClientConfig.Stackoverflow stackoverflow = new ClientConfig.Stackoverflow("", retryPolicy);
        when(clientConfig.stackoverflow()).thenReturn(stackoverflow);
        stackOverflowClient =
            new StackOverflowClient(WebClient.create("http://localhost:8080"), clientConfig, retryBuilder);

        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(123).block();

        assertNotNull(questionResponse);
        assertTrue(questionResponse.items().getFirst().isAnswered());
        assertEquals(questionResponse.items().getFirst().answerCount(), 23);
        assertEquals(questionResponse.items().getFirst().questionId(), 123);
        assertEquals(questionResponse.items().getFirst().lastActivity().toString(), "2024-03-07T21:24:55Z");
    }
}
