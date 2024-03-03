package sharedDTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiErrorResponse(
    @JsonProperty("description") String description,
    @JsonProperty("code") String code,
    @JsonProperty("exceptionName") String exceptionName,
    @JsonProperty("exceptionMessage") String exceptionMessage,
    @JsonProperty("stacktrace") List<String> stacktrace
) {

}
