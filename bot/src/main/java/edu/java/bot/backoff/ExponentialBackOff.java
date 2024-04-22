package edu.java.bot.backoff;

import edu.java.bot.configuration.BackOffProperties;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class ExponentialBackOff extends CustomRetry {

    public ExponentialBackOff(@NotNull BackOffProperties backOffProperties) {
        super(backOffProperties);
    }

    @Override
    public Duration duration(int attempt) {
        long backOff = (long) (Math.pow(multiplying, attempt) * baseTime);
        return Duration.ofMillis(backOff);
    }
}
