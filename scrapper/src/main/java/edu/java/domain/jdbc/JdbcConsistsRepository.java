package edu.java.domain.jdbc;

import edu.java.domain.dto.ConsistDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JdbcConsistsRepository {
    private JdbcTemplate jdbcTemplate;

    public void add(Long chatId, Long linkId) {
        jdbcTemplate.update(
            "INSERT INTO consists (chat_id, link_id) VALUES (?, ?)",
            chatId,
            linkId
        );
    }

    public List<ConsistDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM consists",
            new DataClassRowMapper<>(ConsistDto.class)
        );
    }
}
