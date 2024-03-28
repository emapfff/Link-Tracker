package edu.java.domain.dto;

public record StackOverflowDto(
    Long id,
    Long linkId,
    Integer answerCount
) {
}
