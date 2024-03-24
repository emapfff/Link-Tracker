package edu.java.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QuestionResponse(
    List<ItemResponse> items
) {
    public record ItemResponse(@JsonProperty("is_answered") Boolean isAnswered,
                               @JsonProperty("answer_count") Integer answerCount,
                               @JsonProperty("question_id") Long questionId,
                               @JsonProperty("last_activity_date") OffsetDateTime lastActivity
    ) {}
}
