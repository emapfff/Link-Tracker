package edu.java.service;

import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;

public interface LinkService {
    LinkDto add(Integer tgChatId, URI url, OffsetDateTime lastUpdate);

    LinkDto remove(Integer tgChatId, URI url);

    Collection<LinkDto> listAll(Integer tgChatId);
}
