package edu.java.bot.clients;

import dto.AddLinkRequest;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class ScrapperClient {
    private final String link = "/links";
    private final String tgChat = "/tg-chat/";
    private final String chatId = "Tg-Chat-Id";
    private final WebClient scrapperWebClient;

    public Mono<Void> registrationChat(Integer id) {
        log.info("chat registration");
        return this.scrapperWebClient
            .post()
            .uri(tgChat + id)
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<Void> removeChat(Integer id) {
        log.info("removing chat");
        return this.scrapperWebClient
            .post()
            .uri(tgChat + id)
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<ListLinksResponse> getLinks(Integer tgChatId) {
        log.info("getting links");
        return this.scrapperWebClient
            .get()
            .uri(link)
            .header(chatId, tgChatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<LinkResponse> addLink(Integer tgChatId, AddLinkRequest addLinkRequest) {
        log.info("adding link");
        return this.scrapperWebClient
            .post()
            .uri(link)
            .header(chatId, tgChatId.toString())
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    public Mono<LinkResponse> deleteLink(Integer tgChatId, RemoveLinkRequest removeLinkRequest) {
        log.info("deleting link");
        return this.scrapperWebClient
            .method(HttpMethod.DELETE)
            .uri(link)
            .header(chatId, tgChatId.toString())
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }
 }
