package edu.java.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BranchResponse(
    @JsonProperty("name") String name,
    @JsonProperty("commit") Commit commit,
    @JsonProperty("protected") Boolean protect
) {
    public record Commit(
        @JsonProperty("sha") String sha,
        @JsonProperty("url") String url
    ) {
    }
}
