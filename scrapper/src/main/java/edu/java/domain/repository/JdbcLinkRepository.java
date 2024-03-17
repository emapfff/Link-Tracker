package edu.java.domain.repository;

import edu.java.domain.dto.LinkDto;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcLinkRepository implements EntityOperations<LinkDto> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void add(LinkDto linkDto) {
        jdbcTemplate.update(
            "INSERT INTO link (url, last_update) VALUES (?, ?)",
            linkDto.getUrl(),
            Timestamp.valueOf(linkDto.getLast_update().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime())
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
                linkDto.setUrl(rs.getString("url"));
                linkDto.setLast_update(rs.getTimestamp("last_update").toInstant().atOffset(ZoneOffset.UTC));
                return linkDto;
            }
        );
    }
}
