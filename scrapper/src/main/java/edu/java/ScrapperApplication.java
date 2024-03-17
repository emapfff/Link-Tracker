package edu.java;

import edu.java.configuration.ApplicationConfigScrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfigScrapper.class)
public class ScrapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }

}
