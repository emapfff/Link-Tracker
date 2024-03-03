package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LinkResponse(
    @JsonProperty("id") Integer id,
    @JsonProperty("url") URI url
) {}
