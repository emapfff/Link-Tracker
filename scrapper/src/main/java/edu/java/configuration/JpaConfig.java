package edu.java.configuration;

import edu.java.domain.jpa.JpaChatRepository;
import edu.java.domain.jpa.JpaGithubLinkRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaStackOverflowLinkRepository;
import edu.java.domain.jpa.bases.BaseJpaChatRepository;
import edu.java.domain.jpa.bases.BaseJpaGithubLinkRepository;
import edu.java.domain.jpa.bases.BaseJpaLinkRepository;
import edu.java.domain.jpa.bases.BaseJpaStackOverflowLinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaConfig {
    @Bean
    @Primary
    public JpaChatRepository jpaChatRepository(BaseJpaChatRepository baseJpaChatRepository) {
        return new JpaChatRepository(baseJpaChatRepository);
    }

    @Bean
    @Primary
    public JpaLinkRepository jpaLinkRepository(
        BaseJpaLinkRepository baseJpaLinkRepository,
        BaseJpaChatRepository baseJpaChatRepository
    ) {
        return new JpaLinkRepository(baseJpaLinkRepository, baseJpaChatRepository);
    }

    @Bean
    @Primary
    public JpaGithubLinkRepository jpaGithubLinkRepository(
        BaseJpaLinkRepository baseJpaLinkRepository,
        BaseJpaGithubLinkRepository baseJpaGithubLinkRepository, BaseJpaChatRepository baseJpaChatRepository
    ) {
        return new JpaGithubLinkRepository(baseJpaGithubLinkRepository, baseJpaLinkRepository, baseJpaChatRepository);
    }

    @Bean
    @Primary
    public JpaStackOverflowLinkRepository jpaStackOverflowLinkRepository(
        BaseJpaChatRepository baseJpaChatRepository, BaseJpaLinkRepository baseJpaLinkRepository,
        BaseJpaStackOverflowLinkRepository baseJpaStackOverflowLinkRepository
    ) {
        return new JpaStackOverflowLinkRepository(baseJpaStackOverflowLinkRepository, baseJpaChatRepository,
            baseJpaLinkRepository
        );
    }

}
