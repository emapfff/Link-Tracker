package edu.java.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubUserResponse(
    @JsonProperty("login") String userName,
    @JsonProperty("updated_at") String lastUpdate
){

}
