package dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.net.URI;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record LinkUpdateRequest(
    Integer id,
    URI url,
    String description,
    List<Integer> tgChatIds
) {}

