package edu.java.domain.repository;

import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcLinkRepository {
    private static final String FIND_ALL_BY_TG_CHAT_ID =
        "SELECT link.id, link.url, link.last_update "
            + "FROM link JOIN consists ON link.id = consists.link_id "
            + "JOIN chat ON chat.id = consists.chat_id WHERE chat.tg_chat_id = ?";

    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void add(Integer tgChatId, URI url, OffsetDateTime lastUpdate) {
        jdbcTemplate.update(
            "INSERT INTO link (url, last_update) VALUES (?, ?)",
            url.toString(),
            Timestamp.valueOf(lastUpdate.toLocalDateTime())
        );
        Integer chatId = findIdByTgChatId(tgChatId);
        Integer linkId = findAllTuplesByUrl(url).getLast().getId();
        jdbcTemplate.update(
            "INSERT INTO consists (chat_id, link_id) VALUES (?, ?)",
            chatId,
            linkId
        );
    }

    @Transactional
    public List<LinkDto> findAllTuplesByUrl(URI url) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE url=?",
            (rs, rowNum) -> getLinkDto(rs),
            url.toString()
        );
    }

    @Transactional
    public Integer findIdByTgChatId(Integer tgChatId) {
        return jdbcTemplate.queryForObject(
            "SELECT id FROM chat WHERE tg_chat_id=?",
            Integer.class,
            tgChatId
        );

    }

    @Transactional
    public void remove(Integer tgChatId, URI url) {
        Integer chatId = findIdByTgChatId(tgChatId);
        Integer linkId = findLinkIdByChatIdAndUrl(tgChatId, url).getId();
        jdbcTemplate.update(
            "DELETE FROM consists WHERE chat_id=? AND link_id=?",
            chatId,
            linkId
        );
        jdbcTemplate.update(
            "DELETE FROM link WHERE url=? and id=?",
            url.toString(),
            linkId
        );
    }

    @Transactional
    public LinkDto findLinkIdByChatIdAndUrl(Integer tgChatId, URI url) {
        return jdbcTemplate.queryForObject(
            FIND_ALL_BY_TG_CHAT_ID + " AND url=?",
            (rs, rowNum) -> getLinkDto(rs),
            tgChatId,
            url.toString()
        );
    }

    @Transactional
    public List<LinkDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM link",
            (rs, rowNum) -> getLinkDto(rs)
        );
    }

    @Transactional
    public List<LinkDto> findAllByTgChatId(Integer tgChatId) {
        return jdbcTemplate.query(
            FIND_ALL_BY_TG_CHAT_ID,
            (rs, rowNum) -> getLinkDto(rs),
            tgChatId
        );
    }

    @NotNull
    private LinkDto getLinkDto(ResultSet rs) throws SQLException {
        LinkDto linkDto = new LinkDto();
        linkDto.setId(rs.getInt("id"));
        linkDto.setUrl(URI.create(rs.getString("url")));
        LocalDateTime localDateTime = rs.getTimestamp("last_update").toLocalDateTime();
        ZoneOffset systemZoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        linkDto.setLastUpdate(localDateTime.atOffset(systemZoneOffset));
        return linkDto;
    }
}
