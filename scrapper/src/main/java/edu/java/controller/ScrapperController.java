package edu.java.controller;

import edu.java.domain.dto.LinkDto;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.service.LinkService;
import edu.java.service.TgChatService;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScrapperController {
    private final TgChatService tgChatService;
    private final LinkService linkService;

    @PostMapping("/tg-chat/{id}")
    public void registrationChat(@PathVariable(value = "id") Long id) {
        tgChatService.register(id);
    }

    @DeleteMapping("/tg-chat/{id}")
    public void removeChat(@PathVariable(value = "id") Long id) {
        tgChatService.unregister(id);
    }

    @GetMapping("/links")
    public ListLinksResponse getLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        Collection<LinkDto> links = linkService.listAll(tgChatId);
        List<LinkResponse> linkResponses = links.stream()
            .map(link -> new LinkResponse(link.id(), link.url()))
            .toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @PostMapping("/links")
    public LinkResponse addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody @NotNull AddLinkRequest addLinkRequest
    ) {
        LinkDto addLink = linkService.add(tgChatId, addLinkRequest.link());
        return new LinkResponse(addLink.id(), addLink.url());
    }

    @DeleteMapping("/links")
    public LinkResponse deleteLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody @NotNull RemoveLinkRequest removeLinkRequest
    ) {
        LinkDto deleteLink = linkService.remove(tgChatId, removeLinkRequest.link());
        return new LinkResponse(deleteLink.id(), deleteLink.url());
    }
}
