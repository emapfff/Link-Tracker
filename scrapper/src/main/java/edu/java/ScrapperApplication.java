package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.configuration.BackOffProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({ApplicationConfig.class, BackOffProperties.class})
@SpringBootApplication
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
