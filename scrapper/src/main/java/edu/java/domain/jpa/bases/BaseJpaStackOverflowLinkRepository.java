package edu.java.domain.jpa.bases;

import edu.java.domain.entity.Link;
import edu.java.domain.entity.StackOverflowLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

@Component
public interface BaseJpaStackOverflowLinkRepository extends JpaRepository<StackOverflowLink, Long> {
    StackOverflowLink findStackOverflowLinkByLink(Link link);

    StackOverflowLink findStackOverflowLinkByLinkId(Long linkId);
}
