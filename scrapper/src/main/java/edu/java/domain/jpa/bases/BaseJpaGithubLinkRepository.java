package edu.java.domain.jpa.bases;

import edu.java.domain.entity.GithubLink;
import edu.java.domain.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

@Component
public interface BaseJpaGithubLinkRepository extends JpaRepository<GithubLink, Long> {
    GithubLink findGithubLinkByLink(Link link);

    GithubLink findGithubLinkByLinkId(Long linkId);
}
