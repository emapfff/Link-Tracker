package edu.java.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dto.LinkUpdateRequest;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@WireMockTest(httpPort = 8080)
class BotClientTest {
    private static BotClient botClient;

    @BeforeAll
    public static void setUp() {
        botClient = new BotClient(WebClient.create("http://localhost:8080"));
    }

    @Test
    void sendUpdate() {
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(
                aResponse()
                    .withStatus(200)
            ));
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            123,
            "http://mycore",
            "updating link",
            Arrays.asList(1, 2, 3)
        );

        StepVerifier.create(botClient.sendUpdate(linkUpdateRequest)).verifyComplete();

        verify(postRequestedFor(urlEqualTo("/updates"))
            .withRequestBody(equalToJson("{\"id\": 123, " +
                "\"url\": \"http://mycore\"," +
                "\"description\": \"updating link\"," +
                "\"tgChatIds\": [1, 2, 3]}")));
    }
}
