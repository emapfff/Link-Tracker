package edu.java.domain.dto;

public record GithubLinkDto(
    Long id,
    Long linkId,
    Integer countBranches
) { }
