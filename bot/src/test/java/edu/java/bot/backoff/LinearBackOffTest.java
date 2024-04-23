package edu.java.bot.backoff;

import edu.java.bot.configuration.RetryBuilder;
import edu.java.bot.configuration.RetryPolicy;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

@SpringBootTest(classes = {LinearBackOff.class, RetryPolicy.class, RetryBuilder.class})
class LinearBackOffTest {
    RetryPolicy retryPolicy = new RetryPolicy();
    @Autowired RetryBuilder retryBuilder;
    private Retry retry;

    @Test
    void generateCompanion() {
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.LINEAR);
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
            .expectNoEvent(Duration.ofSeconds(6))
            .expectError()
            .verify();
    }
}
