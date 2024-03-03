package sharedDTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ListLinksResponse(
    @JsonProperty("links") List<LinkResponse> links,

    @JsonProperty("size") Integer size
)
{}
