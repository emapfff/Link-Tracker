package edu.java.updaters;

import edu.java.clients.StackOverflowClient;
import edu.java.domain.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class StackOverFlowUpdaterTest {
    private final LinkDto link = new LinkDto();
    @Autowired
    private StackOverFlowUpdater stackOverFlowUpdater;
    @MockBean
    private StackOverflowClient stackOverflowClient;

    @BeforeEach
    public void setUp() {
        this.link.setId(1L);
        this.link.setUrl(URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"));
        this.link.setLastUpdate(OffsetDateTime.now());

    }

}
