package edu.java.configuration;

import edu.java.domain.ChatRepository;
import edu.java.domain.ConsistsRepository;
import edu.java.domain.GithubLinkRepository;
import edu.java.domain.LinkRepository;
import edu.java.domain.StackOverflowLinkRepository;
import edu.java.domain.jooq.JooqChatRepository;
import edu.java.domain.jooq.JooqConsistsRepository;
import edu.java.domain.jooq.JooqGithubLinkRepository;
import edu.java.domain.jooq.JooqLinkRepository;
import edu.java.domain.jooq.JooqStackOverflowLinkRepository;
import org.jooq.DSLContext;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqConfig {
    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    @Bean
    public ChatRepository chatRepository(DSLContext dslContext) {
        return new JooqChatRepository(dslContext);
    }

    @Bean
    public LinkRepository linkRepository(DSLContext dslContext) {
        return new JooqLinkRepository(dslContext);
    }

    @Bean
    public ConsistsRepository consistsRepository(DSLContext dslContext) {
        return new JooqConsistsRepository(dslContext);
    }

    @Bean
    public GithubLinkRepository githubLinkRepository(DSLContext dslContext) {
        return new JooqGithubLinkRepository(dslContext);
    }

    @Bean
    public StackOverflowLinkRepository stackOverflowLinkRepository(DSLContext dslContext) {
        return new JooqStackOverflowLinkRepository(dslContext);
    }
}
