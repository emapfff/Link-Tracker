package edu.java.domain.jooq.repository;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JooqLinkRepository {
    private DSLContext dslContext;
}
