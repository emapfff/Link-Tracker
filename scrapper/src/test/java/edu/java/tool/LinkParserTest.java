package edu.java.tool;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = LinkParser.class)
class LinkParserTest {
    @Autowired
    private LinkParser linkParser;

    @Test
    void parseGithub() {
        Resource typeLink = linkParser.parse(URI.create("https://github.com/emapfff/java-backend_2024/"));

        assertEquals(typeLink, Resource.GITHUB);
    }

    @Test
    void parseStackOverFlow() {
        Resource typeLink =
            linkParser.parse(URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"));

        assertEquals(typeLink, Resource.STACKOVERFLOW);
    }

    @Test
    void parseIncorrect() {
        Resource typeLink = linkParser.parse(URI.create("https://mycore"));

        assertEquals(typeLink, Resource.INCORRECT_URL);
    }

    @Test
    void getGithubUser() {
        String user = linkParser.getGithubUser(URI.create("https://github.com/emapfff/java-backend-2024/"));

        assertEquals(user, "emapfff");
    }

    @Test
    void getGithubUserIncorrect() {
        String user = linkParser.getGithubUser(URI.create("https:////github.com////emapfff/java-backend-2024/"));

        assertNotEquals(user, "emapfff");
    }

    @Test
    void getGithubRepo() {
        String repo = linkParser.getGithubRepo(URI.create("https://github.com////emapfff////java-backend-2024/"));

        assertNotEquals(repo, "java-backend-2024");
    }

    @Test
    void getStackOverFlowId() {
        long id = linkParser.getStackOverFlowId(URI.create(
            "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"));

        assertEquals(id, 1642028);
    }
}
