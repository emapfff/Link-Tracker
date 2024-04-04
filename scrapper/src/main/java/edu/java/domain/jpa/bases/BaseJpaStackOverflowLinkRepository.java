package edu.java.domain.jpa.bases;

import edu.java.domain.entity.Link;
import edu.java.domain.entity.StackOverflowLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BaseJpaStackOverflowLinkRepository extends JpaRepository<StackOverflowLink, Long> {
    @Query("SELECT sl FROM StackOverflowLink sl WHERE sl.link = :link")
    StackOverflowLink findStackOverflowLinkByLink(Link link);

    @Query("SELECT sl FROM StackOverflowLink sl join sl.link WHERE sl.link.id = :linkId")
    StackOverflowLink findStackOverflowLinkByLinkId(Long linkId);
}
