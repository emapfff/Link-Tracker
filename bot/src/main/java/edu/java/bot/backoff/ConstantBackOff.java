package edu.java.bot.backoff;

import edu.java.bot.configuration.RetryPolicy.BackOffType;
import java.time.Duration;
import org.springframework.stereotype.Component;
import static edu.java.bot.configuration.RetryPolicy.BackOffType.CONSTANT;

@Component
public class ConstantBackOff extends CustomRetry {

    @Override
    public Duration duration(int attempts) {
        return Duration.ofMillis(this.baseTime);
    }

    @Override
    public BackOffType retryType() {
        return CONSTANT;
    }

    @Override
    public CustomRetry createRetry() {
        return new ConstantBackOff();
    }
}
