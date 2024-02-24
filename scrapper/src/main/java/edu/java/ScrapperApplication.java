package edu.java;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.configuration.ApplicationConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication {
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    @Autowired
    public ScrapperApplication (GitHubClient gitHubClient, StackOverflowClient stackOverflowClient){
        this.gitHubClient = gitHubClient;
        this.stackOverflowClient = stackOverflowClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
    @PostConstruct
    private void process(){

    }
}
