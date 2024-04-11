package edu.java.backoff;

import edu.java.configuration.BackOffProperties;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
public class ExponentialBackOff extends Retry {
    private final long baseTime;
    private final int attempts;
    private final int multiplying;

    public ExponentialBackOff(@NotNull BackOffProperties backOffProperties) {
        this.baseTime = backOffProperties.initialInterval();
        this.attempts = backOffProperties.maxAttempts();
        this.multiplying = backOffProperties.multiplier();
    }

    @Override
    public Publisher<?> generateCompanion(@NotNull Flux<RetrySignal> retrySignals) {
        return retrySignals.flatMap(this::getRetry);
    }

    @NotNull
    Mono<Long> getRetry(Retry.@NotNull RetrySignal rs) {
        if (rs.totalRetries() < attempts) {
            Duration delay = duration((int) rs.totalRetries());
            log.info("# attempt {} with backoff {}s", rs.totalRetries(), delay.toSeconds());
            return Mono.delay(delay).thenReturn(rs.totalRetries());
        } else {
            log.info("attempts exit with error: {}", rs.failure().getMessage());
            throw Exceptions.propagate(rs.failure());
        }
    }

    public Duration duration(int attempt) {
        long backOff = (long) (Math.pow(multiplying, attempt) * baseTime);
        return Duration.ofMillis(backOff);
    }
}
