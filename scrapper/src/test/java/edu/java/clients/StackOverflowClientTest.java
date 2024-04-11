package edu.java.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.backoff.ConstantBackOff;
import edu.java.backoff.ExponentialBackOff;
import edu.java.backoff.LinearBackOff;
import edu.java.configuration.BackOffProperties;
import edu.java.response.QuestionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matchesXPathWithSubMatcher;
import static com.github.tomakehurst.wiremock.client.WireMock.resetAllScenarios;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ExponentialBackOff.class, LinearBackOff.class, ConstantBackOff.class,
    BackOffProperties.class})
@WireMockTest(httpPort = 8080)
class StackOverflowClientTest {
    @Autowired
    private ExponentialBackOff exponentialBackOff;
    @Autowired
    private LinearBackOff linearBackOff;
    @Autowired
    private ConstantBackOff constantBackOff;
    private Retry retry;
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
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("4")
        );
        stubFor(get(urlEqualTo("/questions/123?site=stackoverflow")).inScenario("Check retry for stack")
            .whenScenarioStateIs("4")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("5")
        );
        stubFor(get(urlEqualTo("/questions/123?site=stackoverflow")).inScenario("Check retry for stack")
            .whenScenarioStateIs("5")
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
        retry = exponentialBackOff;
        stackOverflowClient = new StackOverflowClient(WebClient.create("http://localhost:8080"), retry);

        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(123).block();

        assertNotNull(questionResponse);
        assertTrue(questionResponse.items().getFirst().isAnswered());
        assertEquals(questionResponse.items().getFirst().answerCount(), 23);
        assertEquals(questionResponse.items().getFirst().questionId(), 123);
        assertEquals(questionResponse.items().getFirst().lastActivity().toString(), "2024-03-07T21:24:55Z");
    }

    @Test
    void fetchQuestionLinearBlackOff() {
        retry = linearBackOff;
        stackOverflowClient = new StackOverflowClient(WebClient.create("http://localhost:8080"), retry);

        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(123).block();

        assertNotNull(questionResponse);
        assertTrue(questionResponse.items().getFirst().isAnswered());
        assertEquals(questionResponse.items().getFirst().answerCount(), 23);
        assertEquals(questionResponse.items().getFirst().questionId(), 123);
        assertEquals(questionResponse.items().getFirst().lastActivity().toString(), "2024-03-07T21:24:55Z");
    }

    @Test
    void fetchQuestionConstantBlackOff() {
        retry = constantBackOff;
        stackOverflowClient = new StackOverflowClient(WebClient.create("http://localhost:8080"), retry);

        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(123).block();

        assertNotNull(questionResponse);
        assertTrue(questionResponse.items().getFirst().isAnswered());
        assertEquals(questionResponse.items().getFirst().answerCount(), 23);
        assertEquals(questionResponse.items().getFirst().questionId(), 123);
        assertEquals(questionResponse.items().getFirst().lastActivity().toString(), "2024-03-07T21:24:55Z");
    }
}
