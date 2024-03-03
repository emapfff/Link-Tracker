package edu.java.controller;

import dto.AddLinkRequest;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import exceptions.AbsentChatException;
import exceptions.IncorrectParametersExceptions;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ScrapperController {

    @ApiResponse(responseCode = "200", description = "Чат зарегистрирован")
    @PostMapping("/tg-chat/{id}")
    public Mono<Void> registrationChat(@PathVariable(value = "id") Integer id) throws IncorrectParametersExceptions {
        if (id < 0) {
            throw new IncorrectParametersExceptions("");
        }
        return Mono.empty();
    }

    @ApiResponse(responseCode = "200", description = "Чат успешно удалён")
    @DeleteMapping("/tg-chat/{id}")
    public Mono<Void> removeChat(@PathVariable(value = "id") Integer id) throws IncorrectParametersExceptions {
        if (id < 0) {
            throw new IncorrectParametersExceptions("");
        }
        return Mono.empty();
    }

    @ApiResponse(responseCode = "200", description = "Ссылки успешно получены")
    @GetMapping("/links")
    public Mono<ListLinksResponse> getLinks(@Required Integer tgChatId) throws IncorrectParametersExceptions {
        if (tgChatId < 0) {
            throw new IncorrectParametersExceptions("");
        }
        ListLinksResponse listLinksResponse = new ListLinksResponse(null, 0);
        return Mono.just(listLinksResponse);
    }

    @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена")
    @PostMapping("/links")
    public Mono<LinkResponse> addLink(
        @Required Integer tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    )
        throws URISyntaxException, IncorrectParametersExceptions {
        if (tgChatId < 0 || addLinkRequest == null) {
            throw new IncorrectParametersExceptions("");
        }
        LinkResponse linkResponse = new LinkResponse(0, new URI(""));
        return Mono.just(linkResponse);
    }

    @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана")
    @DeleteMapping("/links")
    public Mono<LinkResponse> deleteLink(
        @Required Integer tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) throws URISyntaxException, IncorrectParametersExceptions, AbsentChatException {
        if (tgChatId < 0) {
            throw new IncorrectParametersExceptions("");
        } else if (removeLinkRequest == null) {
            throw new AbsentChatException("");
        }
        LinkResponse linkResponse = new LinkResponse(0, new URI(""));
        return Mono.just(linkResponse);
    }
}
