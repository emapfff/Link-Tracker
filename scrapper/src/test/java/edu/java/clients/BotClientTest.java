package edu.java.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dto.LinkUpdateRequest;
import java.net.URI;
import java.net.URISyntaxException;
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
    void sendUpdate() throws URISyntaxException {
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(
                aResponse()
                    .withStatus(200)
            ));
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            123L,
            new URI("http://mycore"),
            "updating link",
            Arrays.asList(1L, 2L, 3L)
        );
        String expectedRequest = "{\"id\": 123, " +
            "\"url\": \"http://mycore\"," +
            "\"description\": \"updating link\"," +
            "\"tgChatIds\": [1, 2, 3]}";

        botClient.sendUpdate(linkUpdateRequest);

        verify(postRequestedFor(urlEqualTo("/updates"))
            .withRequestBody(equalToJson(expectedRequest)));
    }
}
