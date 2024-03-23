package edu.java.service.jdbc;

import edu.java.domain.repository.JdbcChatRepository;
import edu.java.exceptions.AbsentChatException;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.service.TgChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final static String INCORRECT_ID = "Чат id меньше 0";
    private final JdbcChatRepository jdbcChatRepository;

    @Override
    public void register(long tgChatId) {
        if (tgChatId < 0) {
            throw new IncorrectParametersException(INCORRECT_ID);
        }
        if (jdbcChatRepository.findIdByTgChatId(tgChatId) == 0) {
            jdbcChatRepository.add(tgChatId);
        }
    }

    @Override
    public void unregister(long tgChatId) {
        if (tgChatId < 0) {
            throw new IncorrectParametersException(INCORRECT_ID);
        }
        if (jdbcChatRepository.findIdByTgChatId(tgChatId) == 0) {
            throw new AbsentChatException("Чат не существует");
        }
        jdbcChatRepository.remove(tgChatId);
    }
}
