package edu.java.domain.repository;

import edu.java.domain.dto.LinkDto;
import java.net.URI;
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
public class JdbcLinkRepository implements EntityOperations<LinkDto> {

    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void add(LinkDto linkDto) {
        jdbcTemplate.update(
            "INSERT INTO link (url, last_update) VALUES (?, ?)",
            linkDto.getUrl().toString(),
            Timestamp.valueOf(linkDto.getLastUpdate().toLocalDateTime())
        );
    }

    @Transactional
    @Override
    public int remove(Integer id) {
        return jdbcTemplate.update("DELETE FROM link WHERE id=?", id);
    }

    @Transactional
    @Override
    public List<LinkDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM link",
            (rs, rowNum) -> {
                LinkDto linkDto = new LinkDto();
                linkDto.setId(rs.getInt("id"));
                linkDto.setUrl(URI.create(rs.getString("url")));
                LocalDateTime localDateTime = rs.getTimestamp("last_update").toLocalDateTime();
                ZoneOffset systemZoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
                linkDto.setLastUpdate(localDateTime.atOffset(systemZoneOffset));
                return linkDto;
            }
        );
    }
}
