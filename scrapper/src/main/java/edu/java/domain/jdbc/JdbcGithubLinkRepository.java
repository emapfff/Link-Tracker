package edu.java.domain.jdbc;

import edu.java.domain.dto.GithubLinkDto;
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
public class JdbcGithubLinkRepository {
    private JdbcTemplate jdbcTemplate;

    private JdbcLinkRepository jdbcLinkRepository;

    @Transactional
    public void add(Long tgChatId, URI url, Integer countBranches) {
        Long linkId = jdbcLinkRepository.findLinkByChatIdAndUrl(tgChatId, url).getId();
        jdbcTemplate.update(
            "INSERT INTO github_links (link_id, count_branches) VALUES (?, ?)",
            linkId,
            countBranches
        );
    }

    @Transactional
    public List<GithubLinkDto> findAllByTgChatIdAndUrl(Long tgChatId, URI url) {
        return jdbcTemplate.query(
            """
                SELECT gl.link_id, gl.count_branches, gl.id
                FROM chat c
                JOIN consists co ON c.id = co.chat_id
                JOIN github_links gl ON co.link_id = gl.link_id
                JOIN (
                    SELECT id, url
                    FROM link
                    WHERE url = ?
                ) l ON gl.link_id = l.id
                WHERE c.tg_chat_id = ?;
                """,
            (rs, rowNum) -> getGithubLink(rs),
            url.toString(),
            tgChatId
        );
    }

    @Transactional
    public GithubLinkDto findGithubLinkByLinkId(Long linkId) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM github_links WHERE link_id=?",
            (rs, rowNum) -> getGithubLink(rs),
            linkId
        );
    }

    @Transactional
    public List<GithubLinkDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM github_links",
            (rs, rowNum) -> getGithubLink(rs)
        );
    }

    private GithubLinkDto getGithubLink(ResultSet rs) throws SQLException {
        GithubLinkDto githubLinkDto = new GithubLinkDto();
        githubLinkDto.setLinkId(rs.getLong("link_id"));
        githubLinkDto.setId(rs.getLong("id"));
        githubLinkDto.setCountBranches(rs.getInt("count_branches"));
        return githubLinkDto;
    }
}
