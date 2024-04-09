package edu.java.domain.jdbc;

import edu.java.domain.LinkRepository;
import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private static final String JOIN_TABLES =
        """
                SELECT link.id, link.url, link.last_update
                FROM link
                JOIN consists ON link.id = consists.link_id
                JOIN chat ON chat.id = consists.chat_id
            """;

    private final JdbcConsistsRepository jdbcConsistsRepository;
    private final JdbcChatRepository jdbcChatRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long tgChatId, @NotNull URI url, @NotNull OffsetDateTime lastUpdate) {
        jdbcTemplate.update(
            "INSERT INTO link (url, last_update) VALUES (?, ?)",
            url.toString(),
            Timestamp.from(lastUpdate.toInstant())
        );
        Long chatId = jdbcChatRepository.findIdByTgChatId(tgChatId);
        Long linkId = findAllByUrl(url).getLast().id();
        jdbcConsistsRepository.add(chatId, linkId);
    }

    @Override
    public List<LinkDto> findAllByUrl(@NotNull URI url) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE url=?",
            (rs, rowNum) -> getLinkDto(rs),
            url.toString()
        );
    }

    @Override
    public void remove(Long tgChatId, URI url) {
        Long linkId = findLinkByChatIdAndUrl(tgChatId, url).id();
        jdbcTemplate.update(
            "DELETE FROM link WHERE url=? and id=?",
            url.toString(),
            linkId
        );
    }

    @Override
    public LinkDto findLinkByChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        return jdbcTemplate.queryForObject(
            JOIN_TABLES + " WHERE chat.tg_chat_id = ? AND url=?",
            (rs, rowNum) -> getLinkDto(rs),
            tgChatId,
            url.toString()
        );
    }

    @Override
    public Integer existLinkByUriAndTgChatId(Long tgChatId, @NotNull URI url) {
        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*)
                FROM link
                JOIN consists ON link.id = consists.link_id
                JOIN chat ON chat.id = consists.chat_id
                WHERE chat.tg_chat_id=? AND link.url=?""",
            Integer.class,
            tgChatId,
            url.toString()
        );
    }

    @Override
    public List<LinkDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM link",
            (rs, rowNum) -> getLinkDto(rs)
        );
    }

    @Override
    public List<LinkDto> findAllByTgChatId(Long tgChatId) {
        return jdbcTemplate.query(
            JOIN_TABLES + " WHERE chat.tg_chat_id = ?",
            (rs, rowNum) -> getLinkDto(rs),
            tgChatId
        );
    }

    @Override
    public List<Long> findAllTgChatIdsByUrl(@NotNull URI url) {
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

    @Override
    public void setLastUpdate(@NotNull LinkDto link, OffsetDateTime lastUpdate) {
        jdbcTemplate.update(
            "UPDATE link SET last_update=? WHERE id=? AND url=?",
            lastUpdate,
            link.id(),
            link.url().toString()
        );
    }

    @Contract("_ -> new") @NotNull
    private LinkDto getLinkDto(@NotNull ResultSet rs) throws SQLException {
        return new LinkDto(
            rs.getLong("id"),
            URI.create(rs.getString("url")),
            OffsetDateTime.ofInstant(rs.getTimestamp("last_update").toInstant(), ZoneOffset.ofHours(0))
        );
    }
}
