package edu.java.bot.clients;

import dto.AddLinkRequest;
import dto.ApiErrorResponse;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import edu.java.bot.configuration.ScrapperClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@Slf4j
@EnableConfigurationProperties(ScrapperClientProperties.class)
@RequiredArgsConstructor
public class ScrapperClient {
    private Retry retry;
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

    public Mono<Void> registrationChat(Long id) {
        log.info("chat registration");
        return this.scrapperWebClient
            .post()
            .uri(tgChatPath + id)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new Exception(error.exceptionMessage())))
            )
            .bodyToMono(Void.class)
            .retryWhen(retry);
    }

    public Mono<Void> removeChat(Long id) {
        log.info("removing chat");
        return this.scrapperWebClient
            .post()
            .uri(tgChatPath + id)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.equals(HttpStatus.NOT_FOUND),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new Exception(error.exceptionMessage())))
            )
            .bodyToMono(Void.class)
            .retryWhen(retry);
    }

    public Mono<ListLinksResponse> getLinks(Long tgChatId) {
        log.info("getting links");
        return this.scrapperWebClient
            .get()
            .uri(linkPath)
            .header(chatIdHeader, Long.toString(tgChatId))
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new Exception(error.exceptionMessage())))
            )
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(retry);
    }

    public Mono<LinkResponse> addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        log.info("adding link");
        return this.scrapperWebClient
            .post()
            .uri(linkPath)
            .header(chatIdHeader, Long.toString(tgChatId))
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new Exception(error.exceptionMessage())))
            )
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry);
    }

    public Mono<LinkResponse> deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        log.info("deleting link");
        return this.scrapperWebClient
            .method(HttpMethod.DELETE)
            .uri(linkPath)
            .header(chatIdHeader, Long.toString(tgChatId))
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new Exception(error.exceptionMessage())))
            )
            .onStatus(
                httpStatusCode -> httpStatusCode.equals(HttpStatus.NOT_FOUND),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new Exception(error.exceptionMessage())))
            )
            .bodyToMono(LinkResponse.class)
            .retryWhen(retry);
    }
 }
