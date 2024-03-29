package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.responses.QuestionResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StackOverflowClientTest {

    private static WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void fetchQuestion() {
        stubFor(get(urlEqualTo("/questions/123?site=stackoverflow"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-type", "application/json")
                    .withBody("{ \"items\" : [ { \"is_answered\" : true," +
                        "\"answer_count\" : 23," +
                        "\"question_id\" : \"123\"," +
                        "\"last_activity_date\" : \"1709846695\"}]}"
                    )));
        StackOverflowClient stackOverflowClient = new StackOverflowClient(
            WebClient.create("http://localhost:" + wireMockServer.port()));

        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(123).block();

        assertNotNull(questionResponse);
        assertTrue(questionResponse.items().getFirst().isAnswered());
        assertEquals(questionResponse.items().getFirst().answerCount(), 23);
        assertEquals(questionResponse.items().getFirst().questionId(), 123);
        assertEquals(questionResponse.items().getFirst().lastActivity().toString(), "2024-03-07T21:24:55Z");
    }
}
