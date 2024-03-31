package edu.java.configuration;

import edu.java.domain.jpa.BaseJpaChatRepository;
import edu.java.domain.jpa.JpaChatRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "edu.java.domain.jpa")
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaConfig {
    @Bean
    @Primary
    public JpaChatRepository jpaChatRepository() {
        return new JpaChatRepository();
    }

    @Bean
    @Primary
    public JpaLinkRepository jpaLinkRepository() {
        return new JpaLinkRepository();
    }

}
