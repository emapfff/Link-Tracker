package edu.java.backoff;

import java.time.Duration;
import edu.java.configuration.RetryPolicy;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ExponentialBackOff extends CustomRetry {
    private final int multiplier;

    public ExponentialBackOff(@NotNull RetryPolicy retryPolicy) {
        super(retryPolicy);
        this.multiplier = 2;
    }

    @Override
    public Duration duration(int attempt) {
        long backOff = (long) (Math.pow(multiplier, attempt) * baseTime);
        return Duration.ofMillis(backOff);
    }
}
