package edu.java.service.jdbc;

import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.exceptions.AbsentChatException;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.service.TgChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final static String INCORRECT_ID = "Чат id меньше 0";
    private final JdbcChatRepository jdbcChatRepository;

    @Override
    @Transactional
    public void register(Long tgChatId) {
        if (tgChatId < 0) {
            throw new IncorrectParametersException(INCORRECT_ID);
        }
        if (jdbcChatRepository.existIdByTgChatId(tgChatId) == 0) {
            jdbcChatRepository.add(tgChatId);
        }
    }

    @Override
    @Transactional
    public void unregister(Long tgChatId) {
        if (tgChatId < 0) {
            throw new IncorrectParametersException(INCORRECT_ID);
        }
        if (jdbcChatRepository.existIdByTgChatId(tgChatId) == 0) {
            throw new AbsentChatException("Чат не существует");
        }
        jdbcChatRepository.remove(tgChatId);
    }
}
