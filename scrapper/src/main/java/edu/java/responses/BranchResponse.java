package edu.java.responses;

public record BranchResponse(
    String name,
    Commit commit,
    Boolean protect
) {
    public record Commit(
        String sha,
        String url
    ) {
    }
}
