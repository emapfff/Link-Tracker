package edu.java.domain.jooq.repository;

import edu.java.domain.jooq.tables.records.ConsistsRecord;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.domain.jooq.Tables.CONSISTS;

@Repository
@RequiredArgsConstructor
public class JooqConsistsRepository {
    private final DSLContext dslContext;

    @Transactional
    public void add(Long chatId, Long linkId) {
        dslContext
            .insertInto(CONSISTS)
            .set(CONSISTS.CHAT_ID, chatId)
            .set(CONSISTS.LINK_ID, linkId)
            .execute();
    }

    @Transactional
    public List<ConsistsRecord> findAll() {
        return dslContext.selectFrom(CONSISTS).fetch();
    }
}
