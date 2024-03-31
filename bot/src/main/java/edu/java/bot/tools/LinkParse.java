package edu.java.bot.tools;

import java.net.URI;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class LinkParse {
    private static final String GITHUB_PATTERN = "^https?:\\/\\/github\\.com\\/([a-zA-Z0-9-]+)\\/([a-zA-Z0-9-]+)\\/?$";
    private static final String STACKOVERFLOW_PATTERN =
        "^https?:\\/\\/stackoverflow\\.com\\/questions\\/([0-9]+)\\/([a-zA-Z0-9-]+)\\/?$";

    public Urls parse(@NotNull URI uri) {
        Pattern githubPattern = Pattern.compile(GITHUB_PATTERN);
        Pattern stackoverflowPattern = Pattern.compile(STACKOVERFLOW_PATTERN);
        if (githubPattern.matcher(uri.toString()).matches()) {
            return Urls.GITHUB;
        } else if (stackoverflowPattern.matcher(uri.toString()).matches()) {
            return Urls.STACKOVERFLOW;
        }
        return Urls.INCORRECT_URL;
    }

    public String getGithubUser(@NotNull URI url) {
        return Arrays.stream(url.getPath().split("/")).toList().get(1);
    }

    public String getGithubRepo(@NotNull URI url) {
        return Arrays.stream(url.getPath().split("/")).toList().get(2);
    }

    public Long getStackOverFlowId(@NotNull URI url) {
        return Long.parseLong(Arrays.stream(url.getPath().split("/")).toList().get(2));
    }
}
