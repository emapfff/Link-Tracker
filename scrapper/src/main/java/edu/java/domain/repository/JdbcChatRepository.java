package edu.java.domain.repository;

import edu.java.domain.dto.ChatDto;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcChatRepository implements EntityOperations<ChatDto> {

    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void add(ChatDto chatDto) {
        jdbcTemplate.update(
            "INSERT INTO chat (id, userName, created_at) VALUES (?, ?, ?)",
            chatDto.getId(),
            chatDto.getUserName(),
            Timestamp.valueOf(chatDto.getCreatedAt().toLocalDateTime())
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
                LocalDateTime localDateTime = rs.getTimestamp("created_at").toLocalDateTime();
                ZoneOffset systemZoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
                chatDto.setCreatedAt(localDateTime.atOffset(systemZoneOffset));
                return chatDto;
            }
        );
    }
}
