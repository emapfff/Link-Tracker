package edu.java.domain.jdbc;

import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JdbcLinkRepository {
    private static final String JOIN_TABLES =
        """
                SELECT link.id, link.url, link.last_update
                FROM link JOIN consists ON link.id = consists.link_id
                JOIN chat ON chat.id = consists.chat_id
            """;

    private JdbcConsistsRepository jdbcConsistsRepository;
    private JdbcChatRepository jdbcChatRepository;
    private JdbcTemplate jdbcTemplate;

    public void add(Long tgChatId, URI url, OffsetDateTime lastUpdate) {
        jdbcTemplate.update(
            "INSERT INTO link (url, last_update) VALUES (?, ?)",
            url.toString(),
            Timestamp.from(lastUpdate.toInstant())
        );
        Long chatId = jdbcChatRepository.findIdByTgChatId(tgChatId);
        Long linkId = findAllByUrl(url).getLast().id();
        jdbcConsistsRepository.add(chatId, linkId);
    }

    public List<LinkDto> findAllByUrl(URI url) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE url=?",
            (rs, rowNum) -> getLinkDto(rs),
            url.toString()
        );
    }

    public void remove(Long tgChatId, URI url) {
        Long linkId = findLinkByChatIdAndUrl(tgChatId, url).id();
        jdbcTemplate.update(
            "DELETE FROM link WHERE url=? and id=?",
            url.toString(),
            linkId
        );
    }

    public LinkDto findLinkByChatIdAndUrl(Long tgChatId, URI url) {
        return jdbcTemplate.queryForObject(
            JOIN_TABLES + " WHERE chat.tg_chat_id = ? AND url=?",
            (rs, rowNum) -> getLinkDto(rs),
            tgChatId,
            url.toString()
        );
    }

    public Integer existLinkByUriAndTgChatId(Long tgChatId, URI url) {
        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*)
                                FROM link JOIN consists ON link.id = consists.link_id
                                JOIN chat ON chat.id = consists.chat_id
                WHERE chat.tg_chat_id=? AND link.url=?""",
            Integer.class,
            tgChatId,
            url.toString()
        );
    }

    public List<LinkDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM link",
            (rs, rowNum) -> getLinkDto(rs)
        );
    }

    public List<LinkDto> findAllByTgChatId(Long tgChatId) {
        return jdbcTemplate.query(
            JOIN_TABLES + " WHERE chat.tg_chat_id = ?",
            (rs, rowNum) -> getLinkDto(rs),
            tgChatId
        );
    }

    public List<Long> findAllTgChatIdsByUrl(URI url) {
        return jdbcTemplate.queryForList(
            """
                SELECT tg_chat_id
                FROM link
                JOIN consists ON link.id = consists.link_id
                JOIN chat ON chat.id = consists.chat_id
                WHERE link.url=?""",
            Long.class,
            url.toString()
        );
    }

    public void setLastUpdate(LinkDto link, OffsetDateTime lastUpdate) {
        jdbcTemplate.update(
            "UPDATE link SET last_update=? WHERE id=? AND url=?",
            lastUpdate,
            link.id(),
            link.url().toString()
        );
    }

    @NotNull
    private LinkDto getLinkDto(ResultSet rs) throws SQLException {
        return new LinkDto(
            rs.getLong("id"),
            URI.create(rs.getString("url")),
            OffsetDateTime.ofInstant(rs.getTimestamp("last_update").toInstant(), ZoneOffset.ofHours(0))
        );
    }
}
