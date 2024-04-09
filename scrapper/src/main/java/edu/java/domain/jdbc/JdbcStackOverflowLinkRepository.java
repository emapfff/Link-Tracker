package edu.java.domain.jdbc;

import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.dto.LinkDto;
import edu.java.domain.dto.StackOverflowDto;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class JdbcStackOverflowLinkRepository implements StackOverflowLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkRepository linkRepository;

    @Override
    public void add(Long tgChatId, URI url, Integer answerCount) {
        Long linkId = linkRepository.findLinkByChatIdAndUrl(tgChatId, url).id();
        jdbcTemplate.update(
            "INSERT INTO stackoverflow_link (link_id, answer_count) VALUES (?, ?)",
            linkId,
            answerCount
        );
    }

    @Override
    public StackOverflowDto findStackOverflowLinkByTgChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        try {
            return jdbcTemplate.queryForObject(
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
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public StackOverflowDto findStackOverflowLinkByLinkId(Long linkId) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM stackoverflow_link WHERE link_id=?",
            new DataClassRowMapper<>(StackOverflowDto.class),
            linkId
        );
    }

    @Override
    public List<StackOverflowDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM stackoverflow_link",
            new DataClassRowMapper<>(StackOverflowDto.class)
        );
    }

    @Override
    public void setAnswersCount(LinkDto link, Integer answerCount) {
        jdbcTemplate.update(
            "UPDATE stackoverflow_link SET answer_count=? WHERE link_id=?",
            answerCount,
            link.id()
        );
    }

}
