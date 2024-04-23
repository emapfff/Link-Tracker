package edu.java.domain.jpa.bases;

import edu.java.domain.entity.GithubLink;
import edu.java.domain.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BaseJpaGithubLinkRepository extends JpaRepository<GithubLink, Long> {
    @Query("SELECT gl FROM GithubLink gl WHERE gl.link = :link")
    GithubLink findGithubLinkByLink(Link link);

    @Query("SELECT gl FROM GithubLink gl join gl.link WHERE gl.link.id = :linkId")
    GithubLink findGithubLinkByLinkId(Long linkId);
}
