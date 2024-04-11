package edu.java.bot.backoff;

import edu.java.bot.configuration.BackOffProperties;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
public class LinearBackOff extends Retry {
    private static long previousDelay;
    private final int attempts;
    private final long baseTime;

    public LinearBackOff(@NotNull BackOffProperties backOffProperties) {
        this.attempts = backOffProperties.maxAttempts();
        this.baseTime = backOffProperties.initialInterval();
    }

    @Override
    public Publisher<?> generateCompanion(@NotNull Flux<RetrySignal> retrySignals) {
        previousDelay = 0;
        return retrySignals.flatMap(this::getRetry);
    }

    @NotNull
    Mono<Long> getRetry(@NotNull RetrySignal rs) {
        if (rs.totalRetries() < attempts) {
            Duration delay = duration();
            log.info("# attempt {} with backoff {}s", rs.totalRetries(), delay.toSeconds());
            return Mono.delay(delay).thenReturn(rs.totalRetries());
        } else {
            log.info("attempts exit with error: {}", rs.failure().getMessage());
            throw Exceptions.propagate(rs.failure());
        }
    }

    public Duration duration() {
        previousDelay = previousDelay + baseTime;
        return Duration.ofMillis(previousDelay);
    }

}
