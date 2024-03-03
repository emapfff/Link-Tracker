package edu.java.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sharedDTOs.AddLinkRequest;
import sharedDTOs.LinkResponse;
import sharedDTOs.ListLinksResponse;
import sharedDTOs.RemoveLinkRequest;

@RestController
@Slf4j
public class ScrapperController {

    @ApiResponse(responseCode = "200", description = "Чат зарегистрирован")
    @PostMapping("/tg-chat/{id}")
    public Mono<Void> registrationChat(@PathVariable(value = "id") Integer id) {
        return Mono.empty();
    }

    @ApiResponse(responseCode = "200", description = "Чат успешно удалён")
    @DeleteMapping("/tg-chat/{id}")
    public Mono<Void> removeChat(@PathVariable(value = "id") Integer id) {
        return Mono.empty();
    }

    @ApiResponse(responseCode = "200", description = "Ссылки успешно получены")
    @GetMapping("/links")
    public Mono<ListLinksResponse> getLinks(@Required Integer tgChatId) {
        ListLinksResponse listLinksResponse = new ListLinksResponse(null, 0);
        return Mono.just(listLinksResponse);
    }

    @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена")
    @PostMapping("/links")
    public Mono<LinkResponse> addLinks(@Required Integer tgChatId, @RequestBody AddLinkRequest addLinkRequest)
        throws URISyntaxException {
        LinkResponse linkResponse = new LinkResponse(0, new URI(""));
        return Mono.just(linkResponse);
    }

    @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана")
    @DeleteMapping("/links")
    public Mono<LinkResponse> deleteLinks(
        @Required Integer tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest
    ) throws URISyntaxException {
        LinkResponse linkResponse = new LinkResponse(0, new URI(""));
        return Mono.just(linkResponse);
    }
}
