package edu.java.domain.jdbc;

import edu.java.domain.dto.ChatDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcChatRepository {

    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void add(Long tgChatId) {
        jdbcTemplate.update(
            "INSERT INTO chat (tg_chat_id) VALUES (?)",
            tgChatId
        );
    }

    @Transactional
    public void remove(Long tgChatId) {
        jdbcTemplate.update(
            "DELETE FROM chat WHERE tg_chat_id=?",
            tgChatId
        );
    }

    @Transactional
    public Long findCountIdByTgChatId(Long tgChatId) {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM chat WHERE tg_chat_id=?",
            Long.class,
            tgChatId
        );
    }


    @Transactional
    public Long findIdByTgChatId(Long tgChatId) {
        return jdbcTemplate.queryForObject(
            "SELECT id FROM chat WHERE tg_chat_id=?",
            Long.class,
            tgChatId
        );
    }

    @Transactional
    public List<ChatDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM chat",
            (rs, rowNum) -> {
                ChatDto chatDto = new ChatDto();
                chatDto.setId(rs.getLong("id"));
                chatDto.setTgChatId(rs.getLong("tg_chat_id"));
                return chatDto;
            }
        );
    }
}
