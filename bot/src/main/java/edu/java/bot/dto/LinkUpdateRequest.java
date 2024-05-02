package edu.java.bot.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.net.URI;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record LinkUpdateRequest(
    Long id,
    URI url,
    String description,
    List<Long> tgChatIds
) {}

