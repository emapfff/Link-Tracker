package edu.java.controller;

import dto.AddLinkRequest;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import edu.java.domain.dto.LinkDto;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcTgChatService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class ScrapperController {
    private final JdbcTgChatService chatService;
    private final JdbcLinkService linkService;

    @ApiResponse(responseCode = "200", description = "Чат зарегистрирован")
    @PostMapping("/tg-chat/{id}")
    public void registrationChat(@PathVariable(value = "id") Long id) {
        chatService.register(id);
    }

    @ApiResponse(responseCode = "200", description = "Чат успешно удалён")
    @DeleteMapping("/tg-chat/{id}")
    public void removeChat(@PathVariable(value = "id") Long id) {
        chatService.unregister(id);
    }

    @ApiResponse(responseCode = "200", description = "Ссылки успешно получены")
    @GetMapping("/links")
    public Mono<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        Collection<LinkDto> links = linkService.listAll(tgChatId);
        List<LinkResponse> linkResponses = links.stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList();
        return Mono.just(new ListLinksResponse(linkResponses, linkResponses.size()));
    }

    @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена")
    @PostMapping("/links")
    public Mono<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) {
        LinkDto addLink = linkService.add(tgChatId, addLinkRequest.link());
        return Mono.just(new LinkResponse(addLink.getId(), addLink.getUrl()));
    }

    @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана")
    @DeleteMapping("/links")
    public Mono<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        LinkDto deleteLink = linkService.remove(tgChatId, removeLinkRequest.link());
        return Mono.just(new LinkResponse(deleteLink.getId(), deleteLink.getUrl()));
    }
}
