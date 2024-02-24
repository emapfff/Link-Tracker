package edu.java.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QuestionResponse (
    List<ItemResponse> items
) {
    public record ItemResponse(@JsonProperty("is_answered") boolean is_answered,
                        @JsonProperty("answer_count") int answer_count,
                        @JsonProperty("question_id") long question_id,
                        @JsonProperty("last_activity_date") OffsetDateTime last_activity_date
                        ){}
}
