package edu.java.backoff;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest(classes = {ConstantBackOff.class, RetryPolicy.class}, properties = "retry.back-off-type=constant")
class ConstantBackOffTest {
    @Autowired
    ConstantBackOff constantBackOff;

    @Test
    void generateCompanion() {
        StepVerifier.withVirtualTime(() ->
                Mono.error(new RuntimeException("oops"))
                    .retryWhen(constantBackOff)
            )
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(2))
            .expectNoEvent(Duration.ofSeconds(2))
            .expectNoEvent(Duration.ofSeconds(2))
            .expectNoEvent(Duration.ofSeconds(2))
            .expectNoEvent(Duration.ofSeconds(2))
            .expectError()
            .verify();
    }

}
