package edu.java.domain.repository;

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
    public void add(Integer tgChatId) {
        jdbcTemplate.update(
            "INSERT INTO chat (tg_chat_id) VALUES (?)",
            tgChatId
        );

    }

    @Transactional
    public void remove(Integer tgChatId) {
        jdbcTemplate.update(
            "DELETE FROM chat WHERE tg_chat_id=?",
            tgChatId
        );
    }

    @Transactional
    public List<ChatDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM chat",
            (rs, rowNum) -> {
                ChatDto chatDto = new ChatDto();
                chatDto.setId(rs.getInt("id"));
                chatDto.setTgChatId(rs.getInt("tg_chat_id"));
                return chatDto;
            }
        );
    }
}
