package edu.java.bot.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dto.AddLinkRequest;
import dto.RemoveLinkRequest;
import edu.java.bot.configuration.ScrapperClientProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import java.net.URI;
import java.net.URISyntaxException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@WireMockTest(httpPort = 8080)
class ScrapperClientTest {
    private static ScrapperClient scrapperClient;

    @BeforeEach
    public void setUp() {
        ScrapperClientProperties properties = new ScrapperClientProperties(
            "http://localhost:8010",
            new ScrapperClientProperties.Path("/links", "/tg-chat/"),
            new ScrapperClientProperties.Header("Tg-Chat-Id")
        );
        scrapperClient = new ScrapperClient(WebClient.create("http://localhost:8080"), properties);
    }

    @Test
    void registrationChat() {
        stubFor(post(urlEqualTo("/tg-chat/123"))
            .willReturn(
                aResponse()
                    .withStatus(200)
            )
        );

        scrapperClient.registrationChat(123);

        verify(postRequestedFor(urlEqualTo("/tg-chat/123")));
    }

    @Test
    void removeChat() {
        stubFor(post(urlEqualTo("/tg-chat/123"))
            .willReturn(
                aResponse()
                    .withStatus(200)
            ));

        scrapperClient.removeChat(123);

        verify(postRequestedFor(urlEqualTo("/tg-chat/123")));
    }

    @Test
    void getLinks() {
        stubFor(get(urlEqualTo("/links"))
            .willReturn(
                aResponse()
                    .withStatus(200)

            ));

        StepVerifier.create(scrapperClient.getLinks(123)).verifyComplete();

        verify(getRequestedFor(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(123)))
        );
    }

    @Test
    void addLink() throws URISyntaxException {
        stubFor(post(urlEqualTo("/links"))
            .willReturn(
                aResponse()
                    .withStatus(200)
            ));
        AddLinkRequest addLinkRequest = new AddLinkRequest(new URI("http://mycore"));
        String expectedRequest = "{ \"link\" : \"http://mycore\"}";

        StepVerifier.create(scrapperClient.addLink(123, addLinkRequest)).verifyComplete();

        verify(postRequestedFor(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(123)))
            .withRequestBody(equalToJson(expectedRequest))
        );
    }

    @Test
    void deleteLink() throws URISyntaxException {
        stubFor(delete(urlEqualTo("/links"))
            .willReturn(
                aResponse()
                    .withStatus(200)
            ));

        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(new URI("http://mycore"));
        String expectedRequest = "{ \"link\" : \"http://mycore\"}";

        StepVerifier.create(scrapperClient.deleteLink(123, removeLinkRequest)).verifyComplete();

        verify(deleteRequestedFor(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(123)))
            .withRequestBody(equalToJson(expectedRequest))
        );
    }
}
