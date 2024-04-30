package edu.java.bot.tools;

import java.net.URI;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class LinkParser {
    private static final String GITHUB_PATTERN =
        "^https?:\\/\\/github\\.com\\/([a-zA-Z0-9-]+)\\/([a-zA-Z0-9-]+)\\/?$";
    private static final String STACKOVERFLOW_PATTERN =
        "^https?:\\/\\/stackoverflow\\.com\\/questions\\/([0-9]+)\\/([a-zA-Z0-9-]+)\\/?$";

    public Resource parse(@NotNull URI uri) {
        Pattern githubPattern = Pattern.compile(GITHUB_PATTERN);
        Pattern stackoverflowPattern = Pattern.compile(STACKOVERFLOW_PATTERN);
        if (githubPattern.matcher(uri.toString()).matches()) {
            return Resource.GITHUB;
        } else if (stackoverflowPattern.matcher(uri.toString()).matches()) {
            return Resource.STACKOVERFLOW;
        }
        return Resource.INCORRECT_URL;
    }
}
