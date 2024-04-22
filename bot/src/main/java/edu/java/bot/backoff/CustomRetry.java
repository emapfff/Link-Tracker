package edu.java.bot.backoff;

import edu.java.bot.configuration.BackOffProperties;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;

@Slf4j
public abstract class CustomRetry extends Retry {
    protected static long previousDelay;
    protected int attempts;
    protected long baseTime;
    protected int multiplying;

    public CustomRetry(@NotNull BackOffProperties backOffProperties) {
        this.attempts = backOffProperties.maxAttempts();
        this.baseTime = backOffProperties.initialInterval();
        this.multiplying = backOffProperties.multiplier();
    }

    @Override
    public Publisher<?> generateCompanion(@NotNull Flux<RetrySignal> retrySignals) {
        previousDelay = 0;
        return retrySignals.flatMap(this::getRetry);
    }

    @NotNull
    Mono<Long> getRetry(Retry.@NotNull RetrySignal rs) {
        if (rs.totalRetries() < attempts) {
            Duration delay = duration((int) rs.totalRetries());
            log.debug("# attempt {} with backoff {}s", rs.totalRetries(), delay.toSeconds());
            return Mono.delay(delay).thenReturn(rs.totalRetries());
        } else {
            log.debug("attempts exit with error: {}", rs.failure().getMessage());
            throw Exceptions.propagate(rs.failure());
        }
    }

    public abstract Duration duration(int attempts);

}

