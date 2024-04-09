package edu.java.tool;

import java.net.URI;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class LinkParser {
    private static final String GITHUB_PATTERN = "^https?:\\/\\/github\\.com\\/([a-zA-Z0-9-]+)\\/([a-zA-Z0-9-]+)\\/?$";
    private static final String STACKOVERFLOW_PATTERN =
        "^https?:\\/\\/stackoverflow\\.com\\/questions\\/([0-9]+)\\/([a-zA-Z0-9-]+)\\/?$";

    public Resource parse(URI uri) {
        Pattern githubPattern = Pattern.compile(GITHUB_PATTERN);
        Pattern stackoverflowPattern = Pattern.compile(STACKOVERFLOW_PATTERN);
        if (githubPattern.matcher(uri.toString()).matches()) {
            return Resource.GITHUB;
        } else if (stackoverflowPattern.matcher(uri.toString()).matches()) {
            return Resource.STACKOVERFLOW;
        }
        return Resource.INCORRECT_URL;
    }

    public String getGithubUser(URI url) {
        return Arrays.stream(url.getPath().split("/")).toList().get(1);
    }

    public String getGithubRepo(URI url) {
        return Arrays.stream(url.getPath().split("/")).toList().get(2);
    }

    public Long getStackOverFlowId(URI url) {
        return Long.parseLong(Arrays.stream(url.getPath().split("/")).toList().get(2));
    }
}
