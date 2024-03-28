package edu.java.domain.jdbc;

import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JdbcStackOverflowLinkRepository {

    private JdbcTemplate jdbcTemplate;
    private JdbcLinkRepository linkRepository;

    public void add(Long tgChatId, URI url, Integer answerCount) {
        Long linkId = linkRepository.findLinkByChatIdAndUrl(tgChatId, url).id();
        jdbcTemplate.update(
            "INSERT INTO stackoverflow_link (link_id, answer_count) VALUES (?, ?)",
            linkId,
            answerCount
        );
    }

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
            new DataClassRowMapper<>(StackOverflowDto.class),
            url.toString(),
            tgChatId
        );
    }

    public StackOverflowDto findStackOverflowLinkByLinkId(Long linkId) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM stackoverflow_link WHERE link_id=?",
            new DataClassRowMapper<>(StackOverflowDto.class),
            linkId
        );
    }

    public List<StackOverflowDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM stackoverflow_link",
            new DataClassRowMapper<>(StackOverflowDto.class)
        );
    }

    public void setAnswersCount(LinkDto link, Integer answerCount) {
        jdbcTemplate.update(
            "UPDATE stackoverflow_link SET answer_count=? WHERE link_id=?",
            answerCount,
            link.id()
        );
    }

}
