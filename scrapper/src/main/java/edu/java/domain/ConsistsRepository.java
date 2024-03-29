package edu.java.domain;

import edu.java.domain.dto.ConsistDto;
import java.util.List;

public interface ConsistsRepository {
    void add(Long chatId, Long linkId);

    List<ConsistDto> findAll();
}
