package edu.java.backoff;

import edu.java.configuration.RetryPolicy.BackOffType;
import java.time.Duration;
import org.springframework.stereotype.Component;
import static edu.java.configuration.RetryPolicy.BackOffType.LINEAR;

@Component
public class LinearBackOff extends CustomRetry {

    @Override
    public Duration duration(int attempts) {
        previousDelay = previousDelay + baseTime;
        return Duration.ofMillis(previousDelay);
    }

    @Override
    public BackOffType retryType() {
        return LINEAR;
    }

    @Override
    public CustomRetry createRetry() {
        return new LinearBackOff();
    }
}
