package edu.java.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RepositoryResponse(
    @JsonProperty("name") String repoName,
    @JsonProperty("owner") Owner owner,
    @JsonProperty("updated_at") OffsetDateTime lastUpdate
){
    public record  Owner(
        @JsonProperty("login") String login
    ){
    }
}
