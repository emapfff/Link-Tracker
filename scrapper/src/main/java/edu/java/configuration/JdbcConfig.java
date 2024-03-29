package edu.java.configuration;

import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.domain.jdbc.JdbcConsistsRepository;
import edu.java.domain.jdbc.JdbcGithubLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcStackOverflowLinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfig {
    @Bean
    @Primary
    public JdbcChatRepository jdbcChatRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    @Primary
    public JdbcLinkRepository jdbcLinkRepository(
        JdbcConsistsRepository jdbcConsistsRepository,
        JdbcChatRepository jdbcChatRepository,
        JdbcTemplate jdbcTemplate
    ) {
        return new JdbcLinkRepository(jdbcConsistsRepository, jdbcChatRepository, jdbcTemplate);
    }

    @Bean
    @Primary
    public JdbcConsistsRepository jdbcConsistsRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcConsistsRepository(jdbcTemplate);
    }

    @Bean
    @Primary
    public JdbcGithubLinkRepository jdbcGithubLinkRepository(
        JdbcTemplate jdbcTemplate,
        JdbcLinkRepository jdbcLinkRepository
    ) {
        return new JdbcGithubLinkRepository(jdbcTemplate, jdbcLinkRepository);
    }

    @Bean
    @Primary
    public JdbcStackOverflowLinkRepository jdbcStackOverflowLinkRepository(
        JdbcTemplate jdbcTemplate,
        JdbcLinkRepository jdbcLinkRepository
    ) {
        return new JdbcStackOverflowLinkRepository(jdbcTemplate, jdbcLinkRepository);
    }

}
