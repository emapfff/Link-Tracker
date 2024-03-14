package edu.java.bot.clients;

import dto.AddLinkRequest;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import edu.java.bot.configuration.ScrapperClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@EnableConfigurationProperties(ScrapperClientProperties.class)
public class ScrapperClient {
    private final WebClient scrapperWebClient;
    private final String linkPath;
    private final String tgChatPath;
    private final String chatIdHeader;

    @Autowired
    public ScrapperClient(WebClient scrapperWebClient, ScrapperClientProperties properties) {
        this.scrapperWebClient = scrapperWebClient;
        this.linkPath = properties.path().link();
        this.tgChatPath = properties.path().tgChat();
        this.chatIdHeader = properties.header().chatId();
    }

    public void registrationChat(Integer id) {
        log.info("chat registration");
        this.scrapperWebClient
            .post()
            .uri(tgChatPath + id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void removeChat(Integer id) {
        log.info("removing chat");
        this.scrapperWebClient
            .post()
            .uri(tgChatPath + id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public Mono<ListLinksResponse> getLinks(Integer tgChatId) {
        log.info("getting links");
        return this.scrapperWebClient
            .get()
            .uri(linkPath)
            .header(chatIdHeader, tgChatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<LinkResponse> addLink(Integer tgChatId, AddLinkRequest addLinkRequest) {
        log.info("adding link");
        return this.scrapperWebClient
            .post()
            .uri(linkPath)
            .header(chatIdHeader, tgChatId.toString())
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    public Mono<LinkResponse> deleteLink(Integer tgChatId, RemoveLinkRequest removeLinkRequest) {
        log.info("deleting link");
        return this.scrapperWebClient
            .method(HttpMethod.DELETE)
            .uri(linkPath)
            .header(chatIdHeader, tgChatId.toString())
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }
 }
