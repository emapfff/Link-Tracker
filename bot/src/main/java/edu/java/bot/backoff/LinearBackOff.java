package edu.java.bot.backoff;

import edu.java.bot.configuration.BackOffProperties;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class LinearBackOff extends CustomRetry {
    public LinearBackOff(@NotNull BackOffProperties backOffProperties) {
        super(backOffProperties);
    }

    @Override
    public Duration duration(int attempts) {
        previousDelay = previousDelay + baseTime;
        return Duration.ofMillis(previousDelay);
    }
}
