package edu.java.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetryPolicy {
    public BackOffType backOffType;
    public int maxAttempts;
    public long initialInterval;

    public enum BackOffType {
        EXPONENTIAL,
        LINEAR,
        CONSTANT
    }
}
