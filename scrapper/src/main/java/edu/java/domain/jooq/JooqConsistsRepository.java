package edu.java.domain.jooq;

import edu.java.domain.ConsistsRepository;
import edu.java.domain.dto.ConsistDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.generation.Tables.CONSISTS;

@Repository
@RequiredArgsConstructor
public class JooqConsistsRepository implements ConsistsRepository {
    private final DSLContext dslContext;

    @Override
    public void add(Long chatId, Long linkId) {
        dslContext
            .insertInto(CONSISTS)
            .set(CONSISTS.CHAT_ID, chatId)
            .set(CONSISTS.LINK_ID, linkId)
            .execute();
    }

    @Override
    public List<ConsistDto> findAll() {
        return dslContext
            .selectFrom(CONSISTS)
            .fetchInto(ConsistDto.class);
    }
}
