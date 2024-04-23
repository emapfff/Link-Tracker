package edu.java.domain.jdbc;

import edu.java.domain.GithubLinkRepository;
import edu.java.domain.dto.GithubLinkDto;
import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcGithubLinkRepository implements GithubLinkRepository {
    private final JdbcTemplate jdbcTemplate;

    private final JdbcLinkRepository linkRepository;

    @Override
    public void add(Long tgChatId, URI url, Integer countBranches) {
        Long linkId = linkRepository.findLinkByChatIdAndUrl(tgChatId, url).id();
        jdbcTemplate.update(
            "INSERT INTO github_links (link_id, count_branches) VALUES (?, ?)",
            linkId,
            countBranches
        );
    }

    @Override
    public GithubLinkDto findGithubLinkByTgChatIdAndUrl(Long tgChatId, @NotNull URI url) {
        try {
            return jdbcTemplate.queryForObject(
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
                new DataClassRowMapper<>(GithubLinkDto.class),
                url.toString(),
                tgChatId
            );
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public GithubLinkDto findGithubLinkByLinkId(Long linkId) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM github_links WHERE link_id=?",
            new DataClassRowMapper<>(GithubLinkDto.class),
            linkId
        );
    }

    @Override
    public List<GithubLinkDto> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM github_links",
            new DataClassRowMapper<>(GithubLinkDto.class)
        );
    }

    @Override
    public void setCountBranches(@NotNull LinkDto link, Integer countBranches) {
        jdbcTemplate.update(
            "UPDATE github_links SET count_branches=? WHERE link_id=?",
            countBranches,
            link.id()
        );
    }
}
