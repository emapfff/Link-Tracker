package edu.java.domain.jpa.bases;

import edu.java.domain.entity.Link;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BaseJpaLinkRepository extends JpaRepository<Link, Long> {
    List<Link> findAllByUrl(String url);

    Link findLinkByIdAndUrl(Long id, String url);

}
