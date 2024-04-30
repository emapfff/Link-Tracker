package edu.java.configuration;

import edu.java.backoff.CustomRetry;
import edu.java.configuration.RetryPolicy.BackOffType;
import java.util.EnumMap;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;

@Component
@Slf4j
@Getter
public class RetryBuilder {
    EnumMap<BackOffType, CustomRetry> retryBuilder;

    @Autowired
    public RetryBuilder(List<CustomRetry> retries) {
        this.retryBuilder = new EnumMap<>(BackOffType.class);
        retries.forEach(customRetry -> retryBuilder.put(customRetry.retryType(), customRetry.createRetry()));
    }

    public Retry getRetry(RetryPolicy retryPolicy) {
        CustomRetry retry = retryBuilder.get(retryPolicy.getBackOffType());
        retry.setBaseTime(retryPolicy.getInitialInterval());
        retry.setAttempts(retryPolicy.getMaxAttempts());
        retryBuilder.put(retryPolicy.getBackOffType(), retry.createRetry());
        log.debug("{} {} {} {}", retry.getClass().getSimpleName(), retry.retryType(), retry.getAttempts(),
            retry.getBaseTime()
        );
        return retry;
    }
}
