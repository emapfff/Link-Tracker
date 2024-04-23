package edu.java.bot.clients;

import dto.AddLinkRequest;
import dto.ApiErrorResponse;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import edu.java.bot.configuration.ClientConfig;
import edu.java.bot.configuration.RetryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrapperClient {
    private final WebClient scrapperWebClient;
    private final ClientConfig clientConfig;
    private final RetryBuilder retryBuilder;


    public Mono<Void> registrationChat(Long id) {
        log.info("chat registration");
        return this.scrapperWebClient
            .post()
            .uri(clientConfig.scrapper().path().tgChat() + id)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new Exception(error.exceptionMessage())))
            )
            .bodyToMono(Void.class)
            .retryWhen(retryBuilder.getRetry(clientConfig.scrapper().retryPolicy()));

    }

    public Mono<Void> removeChat(Long id) {
        log.info("removing chat");
        return this.scrapperWebClient
            .post()
            .uri(clientConfig.scrapper().path().tgChat() + id)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.equals(HttpStatus.NOT_FOUND),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new Exception(error.exceptionMessage())))
            )
            .bodyToMono(Void.class)
            .retryWhen(retryBuilder.getRetry(clientConfig.scrapper().retryPolicy()));
    }

    public Mono<ListLinksResponse> getLinks(Long tgChatId) {
        log.info("getting links");
        return this.scrapperWebClient
            .get()
            .uri(clientConfig.scrapper().path().link())
            .header(clientConfig.scrapper().header().chatId(), Long.toString(tgChatId))
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new Exception(error.exceptionMessage())))
            )
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(retryBuilder.getRetry(clientConfig.scrapper().retryPolicy()));
    }

    public Mono<LinkResponse> addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        log.info("adding link");
        return this.scrapperWebClient
            .post()
            .uri(clientConfig.scrapper().path().link())
            .header(clientConfig.scrapper().header().chatId(), Long.toString(tgChatId))
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.equals(HttpStatus.BAD_REQUEST),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(error -> Mono.error(new Exception(error.exceptionMessage())))
            )
            .bodyToMono(LinkResponse.class)
            .retryWhen(retryBuilder.getRetry(clientConfig.scrapper().retryPolicy()));
    }

    public Mono<LinkResponse> deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        log.info("deleting link");
        return this.scrapperWebClient
            .method(HttpMethod.DELETE)
            .uri(clientConfig.scrapper().path().link())
            .header(clientConfig.scrapper().header().chatId(), Long.toString(tgChatId))
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
            .retryWhen(retryBuilder.getRetry(clientConfig.scrapper().retryPolicy()));
    }
}
