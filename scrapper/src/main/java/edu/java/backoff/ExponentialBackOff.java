package edu.java.backoff;

import edu.java.configuration.RetryPolicy.BackOffType;
import java.time.Duration;
import org.springframework.stereotype.Component;
import static edu.java.configuration.RetryPolicy.BackOffType.EXPONENTIAL;

@Component
public class ExponentialBackOff extends CustomRetry {

    @Override
    public Duration duration(int attempt) {
        int multiplier = 2;
        long backOff = (long) (Math.pow(multiplier, attempt) * baseTime);
        return Duration.ofMillis(backOff);
    }

    @Override
    public BackOffType retryType() {
        return EXPONENTIAL;
    }

    @Override
    public CustomRetry createRetry() {
        return new ExponentialBackOff();
    }

}
