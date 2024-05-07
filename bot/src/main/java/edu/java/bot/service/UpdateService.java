package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateService {
    private final Bot bot;

    public void sendUpdate(@NotNull LinkUpdateRequest linkUpdateRequest) {
        for (long chatId : linkUpdateRequest.tgChatIds()) {
            this.bot.executeCommand(new SendMessage(chatId, linkUpdateRequest.description()));
            log.debug("Send to tg chat: {} message: {}", chatId, linkUpdateRequest.description());
        }
    }
}
