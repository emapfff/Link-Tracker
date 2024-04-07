package edu.java.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;

class LinkParserTest {
    @Autowired
    private LinkParser linkParse;

    @Test
    void parseGithub() {
        Resource typeLink = linkParse.parse(URI.create("https://github.com/emapfff/java-backend-2024/"));

        assertEquals(typeLink, Resource.GITHUB);
    }

    @Test
    void parseStackOverFlow() {
        Resource typeLink = linkParse.parse(URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"));

        assertEquals(typeLink, Resource.STACKOVERFLOW);
    }

    @Test
    void parseIncorrect() {
        Resource typeLink = linkParse.parse(URI.create("https://mycore"));

        assertEquals(typeLink, Resource.INCORRECT_URL);
    }

    @Test
    void getGithubUser() {
        String user = linkParse.getGithubUser(URI.create("https://github.com/emapfff/java-backend-2024/"));

        assertEquals(user, "emapfff");
    }

    @Test
    void getGithubRepo() {
        String repo = linkParse.getGithubRepo(URI.create("https://github.com/emapfff/java-backend-2024/"));

        assertEquals(repo, "java-backend-2024");
    }

    @Test
    void getStackOverFlowId() {
        long id = linkParse.getStackOverFlowId(URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"));

        assertEquals(id, 1642028);
    }
}
