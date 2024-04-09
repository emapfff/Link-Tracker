package edu.java.configuration;

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
    public JdbcChatRepository jdbcChatRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public JdbcLinkRepository jdbcLinkRepository(
        JdbcConsistsRepository jdbcConsistsRepository,
        JdbcChatRepository jdbcChatRepository,
        JdbcTemplate jdbcTemplate
    ) {
        return new JdbcLinkRepository(jdbcConsistsRepository, jdbcChatRepository, jdbcTemplate);
    }

    @Bean
    public JdbcConsistsRepository jdbcConsistsRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcConsistsRepository(jdbcTemplate);
    }

    @Bean
    public JdbcGithubLinkRepository jdbcGithubLinkRepository(
        JdbcTemplate jdbcTemplate,
        JdbcLinkRepository jdbcLinkRepository
    ) {
        return new JdbcGithubLinkRepository(jdbcTemplate, jdbcLinkRepository);
    }

    @Bean
    public JdbcStackOverflowLinkRepository jdbcStackOverflowLinkRepository(
        JdbcTemplate jdbcTemplate,
        JdbcLinkRepository jdbcLinkRepository
    ) {
        return new JdbcStackOverflowLinkRepository(jdbcTemplate, jdbcLinkRepository);
    }

}
