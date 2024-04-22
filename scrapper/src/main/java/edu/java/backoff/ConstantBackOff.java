package edu.java.backoff;

import java.time.Duration;
import edu.java.configuration.RetryPolicy;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class  ConstantBackOff extends CustomRetry {

    public ConstantBackOff(@NotNull RetryPolicy retryPolicy) {
        super(retryPolicy);
    }

    @Override
    public Duration duration(int attempts) {
        return Duration.ofMillis(this.baseTime);
    }
}
