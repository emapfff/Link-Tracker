package edu.java.domain.jdbc;

import edu.java.domain.dto.StackOverflowDto;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcStackOverflowLinkRepository {
    private JdbcTemplate jdbcTemplate;

    private JdbcLinkRepository jdbcLinkRepository;

    @Transactional
    public void add(Long tgChatId, URI url, Integer answerCount) {
        Long linkId = jdbcLinkRepository.findLinkByChatIdAndUrl(tgChatId, url).getId();
        jdbcTemplate.update(
            "INSERT INTO stackoverflow_link (link_id, answer_count) VALUES (?, ?)",
            linkId,
            answerCount
        );
    }

    @Transactional
    public List<StackOverflowDto> findAllByTgChatIdAndUrl(Long tgChatId, URI url) {
        return jdbcTemplate.query(
            """
                SELECT sl.link_id, sl.answer_count, sl.id
                FROM chat c
                JOIN consists co ON c.id = co.chat_id
                JOIN stackoverflow_link sl ON co.link_id = sl.link_id
                JOIN (
                    SELECT id, url
                    FROM link
                    WHERE url = ?
                ) l ON sl.link_id = l.id
                WHERE c.tg_chat_id = ?;
                """,
            (rs, rowNum) -> getGithubLink(rs),
            url.toString(),
            tgChatId
        );
    }

    @Transactional
    public StackOverflowDto findStackOverflowLinkByLinkId(Long linkId) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM stackoverflow_link WHERE link_id=?",
            (rs, rowNum) -> getGithubLink(rs),
            linkId
        );
    }

    @Transactional
    public List<StackOverflowDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM stackoverflow_link",
            (rs, rowNum) -> getGithubLink(rs)
        );
    }

    private StackOverflowDto getGithubLink(ResultSet rs) throws SQLException {
        StackOverflowDto stackOverflowDto = new StackOverflowDto();
        stackOverflowDto.setLinkId(rs.getLong("link_id"));
        stackOverflowDto.setId(rs.getLong("id"));
        stackOverflowDto.setAnswerCount(rs.getInt("answer_count"));
        return stackOverflowDto;
    }

}
