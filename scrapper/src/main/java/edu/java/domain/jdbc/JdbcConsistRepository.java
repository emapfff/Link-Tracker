package edu.java.domain.jdbc;

import edu.java.domain.dto.ConsistDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcConsistRepository {
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void add(Long chatId, Long linkId) {
        jdbcTemplate.update(
            "INSERT INTO consists (chat_id, link_id) VALUES (?, ?)",
            chatId,
            linkId
        );
    }

    @Transactional
    public List<ConsistDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM consists",
            (rs, rowNum) -> {
                ConsistDto consistDto = new ConsistDto();
                consistDto.setChatId(rs.getLong("chat_id"));
                consistDto.setLinkId(rs.getLong("link_id"));
                return consistDto;
            }
        );
    }
}
