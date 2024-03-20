package edu.java.scheduler;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.domain.dto.LinkDto;
import edu.java.responses.QuestionResponse;
import edu.java.responses.RepositoryResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class StackOverFlowUpdaterTest {
    private final LinkDto link = new LinkDto();
    @Autowired
    private StackOverFlowUpdater stackOverFlowUpdater;
    @MockBean
    private StackOverflowClient stackOverflowClient;

    @BeforeEach
    public void setUp() {
        this.link.setId(1);
        this.link.setUrl(URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"));
        this.link.setLastUpdate(OffsetDateTime.now());

    }

}
