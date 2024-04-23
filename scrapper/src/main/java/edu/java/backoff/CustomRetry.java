package edu.java.backoff;

import edu.java.configuration.RetryPolicy.BackOffType;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@Setter
@Getter
@Component
public abstract class CustomRetry extends Retry {
    protected static long previousDelay;
    protected int attempts;
    protected long baseTime;

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

    public abstract BackOffType retryType();

    public abstract CustomRetry createRetry();
}
