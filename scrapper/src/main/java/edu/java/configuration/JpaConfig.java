package edu.java.configuration;

import edu.java.domain.ChatRepository;
import edu.java.domain.GithubLinkRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.jpa.JpaChatRepository;
import edu.java.domain.jpa.JpaGithubLinkRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaStackOverflowLinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaConfig {

    @Bean
    public ChatRepository chatRepository() {
        return new JpaChatRepository();
    }

    @Bean
    public LinkRepository linkRepository() {
        return new JpaLinkRepository();
    }

    @Bean
    public GithubLinkRepository githubLinkRepository() {
        return new JpaGithubLinkRepository();
    }

    @Bean
    public StackOverflowLinkRepository stackOverflowLinkRepository() {
        return new JpaStackOverflowLinkRepository();
    }

}
