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

@SpringBootTest(classes = {ConstantBackOff.class, RetryPolicy.class, RetryBuilder.class})
class ConstantBackOffTest {
    RetryPolicy retryPolicy = new RetryPolicy();
    @Autowired RetryBuilder retryBuilder;
    private Retry retry;

    @Test
    void generateCompanion() {
        retryPolicy.setBackOffType(RetryPolicy.BackOffType.CONSTANT);
        retryPolicy.setMaxAttempts(5);
        retryPolicy.setInitialInterval(2000L);

        retry = retryBuilder.getRetry(retryPolicy);

        StepVerifier.withVirtualTime(() ->
                Mono.error(new RuntimeException("oops"))
                    .retryWhen(retry)
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
