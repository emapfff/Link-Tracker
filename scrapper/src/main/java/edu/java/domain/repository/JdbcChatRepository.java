package edu.java.domain.repository;

import edu.java.domain.dto.ChatDto;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcChatRepository implements EntityOperations<ChatDto> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void add(ChatDto chatDto) {
        jdbcTemplate.update(
            "INSERT INTO chat (id, userName, created_at) VALUES (?, ?, ?)",
            chatDto.getId(),
            chatDto.getUserName(),
            Timestamp.valueOf(chatDto.getCreatedAt().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime())
        );
    }

    @Transactional
    @Override
    public int remove(Integer id) {
        return jdbcTemplate.update(
            "DELETE FROM chat WHERE id=?",
            id
        );
    }

    @Transactional
    @Override
    public List<ChatDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM chat",
            (rs, rowNum) -> {
                ChatDto chatDto = new ChatDto();
                chatDto.setId(rs.getInt("id"));
                chatDto.setUserName(rs.getString("username"));
                chatDto.setCreatedAt(rs.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC));
                return chatDto;
            }
        );
    }
}
