package edu.java.domain.jdbc;

import edu.java.domain.ChatRepository;
import edu.java.domain.dto.ChatDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long tgChatId) {
        jdbcTemplate.update(
            "INSERT INTO chat (tg_chat_id) VALUES (?)",
            tgChatId
        );
    }

    @Override
    public void remove(Long tgChatId) {
        jdbcTemplate.update(
            "DELETE FROM chat WHERE tg_chat_id=?",
            tgChatId
        );
    }

    @Override
    public Integer existIdByTgChatId(Long tgChatId) {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM chat WHERE tg_chat_id = ?",
            Integer.class,
            tgChatId
        );
    }

    @Override
    public Long findIdByTgChatId(Long tgChatId) {
        return jdbcTemplate.queryForObject(
            "SELECT id FROM chat WHERE tg_chat_id=?",
            Long.class,
            tgChatId
        );
    }

    @Override
    public List<ChatDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM chat",
            new DataClassRowMapper<>(ChatDto.class)
        );
    }
}
