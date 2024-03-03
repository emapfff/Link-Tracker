package edu.java.bot.clients;

import dto.AddLinkRequest;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ScrapperClient {
    private final String link = "/links";
    private final String chat = "Tg-Chat-Id";
    private final WebClient scrapperWebClient;

    public Mono<Void> registrationChat(Integer id) {
        return this.scrapperWebClient
            .post()
            .uri("tg-chat/{id}", id)
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<Void> removeChat(Integer id) {
        return this.scrapperWebClient
            .post()
            .uri("/tg-chat/{id}", id)
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<ListLinksResponse> getLinks(Integer tgChatId) {
        return this.scrapperWebClient
            .get()
            .uri(link)
            .header(chat, tgChatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<LinkResponse> addLink(Integer tgChatId, AddLinkRequest addLinkRequest) {
        return this.scrapperWebClient
            .post()
            .uri(link)
            .header(chat, tgChatId.toString())
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class);

    }

    public Mono<LinkResponse> deleteLink(Integer tgChatId, RemoveLinkRequest removeLinkRequest) {
        return this.scrapperWebClient
            .method(HttpMethod.DELETE)
            .uri(link)
            .header(chat, tgChatId.toString())
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }
 }
