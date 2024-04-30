package edu.java.backoff;

import edu.java.configuration.RetryBuilder;
import edu.java.configuration.RetryPolicy;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

@SpringBootTest(classes = {ExponentialBackOff.class, RetryPolicy.class, RetryBuilder.class})
class ExponentialBackOffTest {
    RetryPolicy retryPolicy = new RetryPolicy();
    @Autowired RetryBuilder retryBuilder;
    private Retry retry;

    @Test
    void generateCompanion() {
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.EXPONENTIAL);
        retryPolicy.setMaxAttempts(3);
        retryPolicy.setInitialInterval(2000L);

        retry = retryBuilder.getRetry(retryPolicy);

        StepVerifier.withVirtualTime(() ->
                Mono.error(new RuntimeException("oops"))
                    .retryWhen(retry)
            )
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(2))
            .expectNoEvent(Duration.ofSeconds(4))
            .expectNoEvent(Duration.ofSeconds(8))
            .expectError()
            .verify();
    }
}
