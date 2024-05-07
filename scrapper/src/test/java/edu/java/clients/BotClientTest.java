package edu.java.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dto.LinkUpdateRequest;
import edu.java.backoff.ConstantBackOff;
import edu.java.backoff.ExponentialBackOff;
import edu.java.backoff.LinearBackOff;
import edu.java.configuration.ClientConfig;
import edu.java.configuration.RetryBuilder;
import edu.java.configuration.RetryPolicy;
import io.micrometer.core.instrument.Counter;
import java.net.URI;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.resetAllScenarios;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ExponentialBackOff.class, LinearBackOff.class, ConstantBackOff.class, RetryBuilder.class})
@WireMockTest(httpPort = 8080)
class BotClientTest {
    private final RetryPolicy retryPolicy = new RetryPolicy();
    @Autowired
    RetryBuilder retryBuilder;
    @Mock
    private ClientConfig clientConfig;
    @InjectMocks
    private BotClient botClient;
    @Mock
    private Counter messageCounter;

    @BeforeEach
    public void setUp() {
        ClientConfig.Bot bot = new ClientConfig.Bot("", retryPolicy);
        when(clientConfig.bot()).thenReturn(bot);
        botClient = new BotClient(WebClient.create("http://localhost:8080"), clientConfig, retryBuilder, messageCounter);
        stubFor(post(urlEqualTo("/updates")).inScenario("Check retry for bot")
            .whenScenarioStateIs(STARTED)
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("2")
        );
        stubFor(post(urlEqualTo("/updates")).inScenario("Check retry for bot")
            .whenScenarioStateIs("2")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("3")
        );
    }

    @AfterEach
    public void stop() {
        resetAllScenarios();
    }

    @ParameterizedTest
    @EnumSource(RetryPolicy.BackOffType.class)
    void sendUpdateTest(RetryPolicy.BackOffType backOffType) {
        stubFor(post(urlEqualTo("/updates")).inScenario("Check retry for bot")
            .whenScenarioStateIs("3")
            .willReturn(aResponse().withStatus(200)));
        retryPolicy.setBackOffType(backOffType);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            123L,
            URI.create("http://mycore"),
            "updating link",
            Arrays.asList(1L, 2L, 3L)
        );
        String expectedRequest = "{\"id\": 123, " +
            "\"url\": \"http://mycore\"," +
            "\"description\": \"updating link\"," +
            "\"tgChatIds\": [1, 2, 3]}";

        botClient.send(linkUpdateRequest);

        verify(postRequestedFor(urlEqualTo("/updates"))
            .withRequestBody(equalToJson(expectedRequest)));

    }

    @ParameterizedTest
    @EnumSource(RetryPolicy.BackOffType.class)
    void failSendUpdateTest(RetryPolicy.BackOffType backOffType) {
        stubFor(post(urlEqualTo("/updates")).inScenario("Check retry for bot")
            .whenScenarioStateIs("3")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("4")
        );
        stubFor(post(urlEqualTo("/updates")).inScenario("Check retry for bot")
            .whenScenarioStateIs("4")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("5")
        );
        stubFor(post(urlEqualTo("/updates")).inScenario("Check retry for bot")
            .whenScenarioStateIs("5")
            .willReturn(aResponse().withStatus(200))
        );
        retryPolicy.setBackOffType(backOffType);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            123L,
            URI.create("http://mycore"),
            "updating link",
            Arrays.asList(1L, 2L, 3L)
        );

        assertThrows(Exception.class, () -> {
            botClient.send(linkUpdateRequest);
        });
    }

}
