package edu.java.bot.backoff;

import edu.java.bot.configuration.BackOffProperties;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class ConstantBackOff extends CustomRetry {

    public ConstantBackOff(@NotNull BackOffProperties backOffProperties) {
        super(backOffProperties);
    }

    @Override
    public Duration duration(int attempts) {
        return Duration.ofMillis(this.baseTime);
    }
}
