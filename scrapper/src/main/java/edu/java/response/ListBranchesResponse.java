package edu.java.response;

import java.util.List;

public record ListBranchesResponse(
    List<BranchResponse> listBranches
) {
}
