package edu.java.domain.jdbc;

import edu.java.domain.ConsistsRepository;
import edu.java.domain.dto.ConsistDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcConsistsRepository implements ConsistsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long chatId, Long linkId) {
        jdbcTemplate.update(
            "INSERT INTO consists (chat_id, link_id) VALUES (?, ?)",
            chatId,
            linkId
        );
    }

    @Override
    public List<ConsistDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM consists",
            new DataClassRowMapper<>(ConsistDto.class)
        );
    }
}
