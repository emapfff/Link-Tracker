package sharedDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record LinkUpdateRequest(
    @JsonProperty Integer id,
    @JsonProperty String url,
    @JsonProperty String description,
    @JsonProperty List<Integer> tgChatIds
) {

}

