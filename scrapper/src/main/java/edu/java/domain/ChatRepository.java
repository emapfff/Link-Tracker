package edu.java.domain;

import edu.java.domain.dto.ChatDto;
import java.util.List;

public interface ChatRepository {
    void add(Long tgChatId);

    void remove(Long tgChatId);

    Integer existIdByTgChatId(Long tgChatId);

    Long findIdByTgChatId(Long tgChatId);

    List<ChatDto> findAll();

}
