package edu.java.controller;

import dto.AddLinkRequest;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import edu.java.exceptions.AbsentChatException;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.exceptions.LinkNotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ScrapperController {
    private static final String INCORRECT_PARAMETERS = "Некорректные параметры запроса";
    private static final String ABSENT_CHAT = "Чат не существует";
    private static final String LINK_NOT_FOUND = "Ссылка не найдена";

    @ApiResponse(responseCode = "200", description = "Чат зарегистрирован")
    @PostMapping("/tg-chat/{id}")
    public Mono<Void> registrationChat(@PathVariable(value = "id") Integer id) {
        if (id < 0) {
            throw new IncorrectParametersException(INCORRECT_PARAMETERS);
        }
        return Mono.empty();
    }

    @ApiResponse(responseCode = "200", description = "Чат успешно удалён")
    @DeleteMapping("/tg-chat/{id}")
    public Mono<Void> removeChat(@PathVariable(value = "id") Integer id) {
        if (id < 0) {
            throw new IncorrectParametersException(INCORRECT_PARAMETERS);
        } else if (id == 0) { // тут будет проверка на отсутствия чата в бд
            throw new AbsentChatException(ABSENT_CHAT);
        }
        return Mono.empty();
    }

    @ApiResponse(responseCode = "200", description = "Ссылки успешно получены")
    @GetMapping("/links")
    public Mono<ListLinksResponse> getLinks(Integer tgChatId) {
        if (tgChatId < 0) {
            throw new IncorrectParametersException(INCORRECT_PARAMETERS);
        }
        ListLinksResponse listLinksResponse = new ListLinksResponse(null, 0);
        return Mono.just(listLinksResponse);
    }

    @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена")
    @PostMapping("/links")
    public Mono<LinkResponse> addLink(
        Integer tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    )
        throws URISyntaxException {
        if (tgChatId < 0) {
            throw new IncorrectParametersException(INCORRECT_PARAMETERS);
        } else if (addLinkRequest.link() == null) { //потом будет проверка на валидность линки
            throw new IncorrectParametersException(INCORRECT_PARAMETERS);
        }
        LinkResponse linkResponse = new LinkResponse(0, new URI(""));
        return Mono.just(linkResponse);
    }

    @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана")
    @DeleteMapping("/links")
    public Mono<LinkResponse> deleteLink(
        Integer tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) throws URISyntaxException {
        if (tgChatId < 0) {
            throw new IncorrectParametersException(INCORRECT_PARAMETERS);
        } else if (removeLinkRequest.link() == null) { // тут будет проверка на то, что есть линк в чате в бд или нет
            throw new LinkNotFoundException(LINK_NOT_FOUND);
        }
        LinkResponse linkResponse = new LinkResponse(0, new URI(""));
        return Mono.just(linkResponse);
    }
}
