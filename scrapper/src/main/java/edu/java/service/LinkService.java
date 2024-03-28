package edu.java.service;

import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.util.Collection;

public interface LinkService {
    LinkDto add(Long tgChatId, URI url);

    LinkDto remove(Long tgChatId, URI url);

    Collection<LinkDto> listAll(Long tgChatId);
}
