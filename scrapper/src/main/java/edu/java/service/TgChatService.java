package edu.java.service;

import edu.java.domain.ChatRepository;
import edu.java.exceptions.AbsentChatException;
import edu.java.exceptions.RepeatRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TgChatService {
    private final ChatRepository chatRepository;

    @Transactional
    public void register(Long tgChatId) {
        if (chatRepository.existIdByTgChatId(tgChatId) != 0) {
            throw new RepeatRegistrationException("Чат уже был добавлен");
        }
        chatRepository.add(tgChatId);
    }

    @Transactional
    public void unregister(Long tgChatId) {
        if (chatRepository.existIdByTgChatId(tgChatId) == 0) {
            throw new AbsentChatException("Чат не существует");
        }
        chatRepository.remove(tgChatId);
    }
}
