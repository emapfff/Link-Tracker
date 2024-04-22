package edu.java.backoff;

import edu.java.configuration.RetryConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest(classes = {LinearBackOff.class, RetryPolicy.class}, properties = "retry.back-off-type=linear")
class LinearBackOffTest {
    @Autowired
    LinearBackOff linearBackOff;

    @Test
    void generateCompanion() {
        StepVerifier.withVirtualTime(() ->
                Mono.error(new RuntimeException("oops"))
                    .retryWhen(linearBackOff)
            )
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(2))
            .expectNoEvent(Duration.ofSeconds(4))
            .expectNoEvent(Duration.ofSeconds(6))
            .expectNoEvent(Duration.ofSeconds(8))
            .expectNoEvent(Duration.ofSeconds(10))
            .expectError()
            .verify();
    }
}
