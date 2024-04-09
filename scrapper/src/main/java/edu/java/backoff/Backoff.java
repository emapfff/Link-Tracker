package edu.java.backoff;

import java.time.Duration;

public interface Backoff {
    Duration duration(int attempt);
}
