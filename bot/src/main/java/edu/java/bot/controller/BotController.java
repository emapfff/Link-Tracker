package edu.java.bot.controller;

import dto.LinkUpdateRequest;
import edu.java.bot.service.Bot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BotController {
    private final Bot bot;

    @PostMapping("/updates")
    public void sendUpdates(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        log.info("{} {}", linkUpdateRequest.description(), linkUpdateRequest.url());
    }

}

