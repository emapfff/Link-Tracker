package edu.java.service.jdbc;

import edu.java.domain.dto.LinkDto;
import edu.java.domain.repository.JdbcChatRepository;
import edu.java.domain.repository.JdbcLinkRepository;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JdbcLinkService implements LinkService {
    private final static String CHAT_NOT_FOUND = "Чат не был добавлен";
    private final static String LINK_NOT_FOUND = "Ссылка не найдена";
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcChatRepository jdbcChatRepository;

    @Override
    public LinkDto add(Integer tgChatId, URI url, OffsetDateTime lastUpdate) {
        if (jdbcChatRepository.findIdByTgChatId(tgChatId) == 0) {
            throw new IncorrectParametersException(CHAT_NOT_FOUND);
        }
        jdbcLinkRepository.add(tgChatId, url, lastUpdate);
        return jdbcLinkRepository.findAllTuplesByUrl(url).getLast();
    }

    @Override
    public LinkDto remove(Integer tgChatId, URI url) {
        if (jdbcChatRepository.findIdByTgChatId(tgChatId) == 0) {
            throw new IncorrectParametersException(CHAT_NOT_FOUND);
        }
        try {
            LinkDto removingLink = jdbcLinkRepository.findLinkIdByChatIdAndUrl(tgChatId, url);
            jdbcLinkRepository.remove(tgChatId, url);
            return removingLink;
        } catch (Exception exception) {
            throw new IncorrectParametersException(LINK_NOT_FOUND);
        }
    }

    @Override
    public Collection<LinkDto> listAll(Integer tgChatId) {
        List<LinkDto> dtoList = jdbcLinkRepository.findAllByTgChatId(tgChatId);
        if (dtoList.isEmpty()) {
            throw new IncorrectParametersException(CHAT_NOT_FOUND);
        }
        return dtoList;
    }
}
