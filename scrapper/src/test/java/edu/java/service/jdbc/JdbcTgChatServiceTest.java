package edu.java.service.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.exceptions.AbsentChatException;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.service.jdbc.JdbcTgChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JdbcTgChatServiceTest {

    private JdbcChatRepository chatRepository;
    private JdbcTgChatService chatService;

    @BeforeEach
    void setUp() {
        chatRepository = mock(JdbcChatRepository.class);
        chatService = new JdbcTgChatService(chatRepository);
    }

    @Test
    void registerValidIdTest() {
        Long tgChatId = 123456L;
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(0);

        assertDoesNotThrow(() -> chatService.register(tgChatId));
        verify(chatRepository, times(1)).add(tgChatId);
    }

    @Test
    void registerInvalidIdTest() {
        Long tgChatId = -1L;
        assertThrows(IncorrectParametersException.class, () -> chatService.register(tgChatId));
        verify(chatRepository, never()).add(tgChatId);
    }

    @Test
    void registerExistingChatTest() {
        Long tgChatId = 123456L;
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(1);

        assertDoesNotThrow(() -> chatService.register(tgChatId));
        verify(chatRepository, never()).add(tgChatId);
    }

    @Test
    void unregisterValidIdTest() {
        Long tgChatId = 123456L;
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(1);

        assertDoesNotThrow(() -> chatService.unregister(tgChatId));
        verify(chatRepository, times(1)).remove(tgChatId);
    }

    @Test
    void unregisterInvalidIdTest() {
        Long tgChatId = -1L;
        assertThrows(IncorrectParametersException.class, () -> chatService.unregister(tgChatId));
        verify(chatRepository, never()).remove(tgChatId);
    }

    @Test
    void unregisterNonExistingChatTest() {
        Long tgChatId = 123456L;
        when(chatRepository.existIdByTgChatId(tgChatId)).thenReturn(0);

        assertThrows(AbsentChatException.class, () -> chatService.unregister(tgChatId));
        verify(chatRepository, never()).remove(tgChatId);
    }
}
