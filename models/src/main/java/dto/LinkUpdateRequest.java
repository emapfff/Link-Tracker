package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record LinkUpdateRequest(
    @JsonProperty("id") Integer id,
    @JsonProperty("url") String url,
    @JsonProperty("description") String description,
    @JsonProperty("tgChatIds") List<Integer> tgChatIds
) {}

