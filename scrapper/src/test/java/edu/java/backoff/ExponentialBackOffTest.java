package edu.java.backoff;

import java.time.Duration;
import edu.java.configuration.BackOffProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = {ExponentialBackOff.class, BackOffProperties.class}, properties = "retry.back-off-type=exponential")
class ExponentialBackOffTest {
    @Autowired
    ExponentialBackOff exponentialBackOff;

    @Test
    void generateCompanion() {
        StepVerifier.withVirtualTime(() ->
                Mono.error(new RuntimeException("oops"))
                    .retryWhen(exponentialBackOff)
            )
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(2))
            .expectNoEvent(Duration.ofSeconds(4))
            .expectNoEvent(Duration.ofSeconds(8))
            .expectNoEvent(Duration.ofSeconds(16))
            .expectNoEvent(Duration.ofSeconds(32))
            .expectError()
            .verify();
    }
}
