package edu.java.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class RetryPolicy {
    private BackOffType backOffType;
    private int maxAttempts;
    private long initialInterval;

    public enum BackOffType {
        EXPONENTIAL,
        CONSTANT,
        LINEAR
    }
}
