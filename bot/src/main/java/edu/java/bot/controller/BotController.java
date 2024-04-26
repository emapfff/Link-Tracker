package edu.java.bot.controller;

import dto.LinkUpdateRequest;
import edu.java.bot.service.Listener;
import edu.java.bot.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotController implements Listener {
    private final UpdateService updateService;

    @Override
    @PostMapping("/updates")
    public void listen(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        log.info("HTTP send message!");
        log.info("{} {}", linkUpdateRequest.description(), linkUpdateRequest.url());
        updateService.sendUpdate(linkUpdateRequest);
    }
}

