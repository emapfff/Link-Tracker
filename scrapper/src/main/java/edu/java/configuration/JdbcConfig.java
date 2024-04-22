package edu.java.configuration;

import edu.java.domain.ChatRepository;
import edu.java.domain.ConsistsRepository;
import edu.java.domain.GithubLinkRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.domain.jdbc.JdbcConsistsRepository;
import edu.java.domain.jdbc.JdbcGithubLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcStackOverflowLinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfig {
    @Bean
    public ChatRepository chatRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public ConsistsRepository consistsRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcConsistsRepository(jdbcTemplate);
    }

    @Bean
    public LinkRepository linkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public GithubLinkRepository githubLinkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcGithubLinkRepository(jdbcTemplate);
    }

    @Bean
    public StackOverflowLinkRepository stackOverflowLinkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcStackOverflowLinkRepository(jdbcTemplate);
    }

}
