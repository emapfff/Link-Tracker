package edu.java.domain.jpa;

import edu.java.domain.ChatRepository;
import edu.java.domain.dto.ChatDto;
import edu.java.domain.entity.Chat;
import edu.java.domain.jpa.bases.BaseJpaChatRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaChatRepository implements ChatRepository {

    private final BaseJpaChatRepository baseJpaChatRepository;

    @Override
    public void add(Long tgChatId) {
        baseJpaChatRepository.save(new Chat(tgChatId));
    }

    @Override
    public void remove(Long tgChatId) {
        baseJpaChatRepository.deleteChatByTgChatId(tgChatId);
    }

    @Override
    public Integer existIdByTgChatId(Long tgChatId) {
        return baseJpaChatRepository.countByTgChatId(tgChatId);
    }

    @Override
    public Long findIdByTgChatId(Long tgChatId) {
        return baseJpaChatRepository.findIdByTgChatId(tgChatId);
    }

    public List<Chat> getChat() {
        return baseJpaChatRepository.findAll();
    }

    @Override
    public List<ChatDto> findAll() {
        List<Chat> chats = baseJpaChatRepository.findAll();
        return chats.stream()
            .map(chat -> new ChatDto(chat.getId(), chat.getTgChatId()))
            .collect(Collectors.toList());
    }
}
