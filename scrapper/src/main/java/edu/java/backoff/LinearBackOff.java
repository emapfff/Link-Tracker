package edu.java.backoff;

import java.time.Duration;
import edu.java.configuration.RetryPolicy;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class LinearBackOff extends CustomRetry {
    public LinearBackOff(@NotNull RetryPolicy retryPolicy) {
        super(retryPolicy);
    }

    @Override
    public Duration duration(int attempts) {
        previousDelay = previousDelay + baseTime;
        return Duration.ofMillis(previousDelay);
    }
}
