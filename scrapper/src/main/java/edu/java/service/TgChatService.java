package edu.java.service;

import edu.java.domain.ChatRepository;
import edu.java.exceptions.AbsentChatException;
import edu.java.exceptions.IncorrectParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TgChatService {
    private final static String INCORRECT_ID = "Чат id меньше 0";
    @Autowired
    private ChatRepository chatRepository;

    @Transactional
    public void register(Long tgChatId) {
        if (tgChatId < 0) {
            throw new IncorrectParametersException(INCORRECT_ID);
        }
        if (chatRepository.existIdByTgChatId(tgChatId) == 0) {
            chatRepository.add(tgChatId);
        }
    }

    @Transactional
    public void unregister(Long tgChatId) {
        if (tgChatId < 0) {
            throw new IncorrectParametersException(INCORRECT_ID);
        }
        if (chatRepository.existIdByTgChatId(tgChatId) == 0) {
            throw new AbsentChatException("Чат не существует");
        }
        chatRepository.remove(tgChatId);
    }
}
