package edu.java.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.responses.QuestionResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
                        "\"last_activity_date\" : \"2024-02-09T17:47:19Z\"}]}"
                    )));
        StackOverflowClient stackOverflowClient = new StackOverflowClient("http://localhost:" + wireMockServer.port());

        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(123);
        QuestionResponse.ItemResponse itemResponse = questionResponse.items().getFirst();

        assertTrue(itemResponse.isAnswered());
        assertEquals(itemResponse.answerCount(), 23);
        assertEquals(itemResponse.questionId(), 123);
        assertEquals(itemResponse.lastActivity().toString(), "2024-02-09T17:47:19Z");
    }
}
