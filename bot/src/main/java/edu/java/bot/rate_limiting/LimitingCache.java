package edu.java.bot.rate_limiting;

import edu.java.bot.configuration.RateLimitingProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LimitingCache {
    private final RateLimitingProperties rateLimitingProperties;
    private Bandwidth bandwidth;

    @Cacheable(value = "rate-limiting-buckets-cache", key = "#root.args[0]")
    public Bucket getBucket(String ip) {
        return buildNewBucket();
    }

    @PostConstruct
    void buildBucket() {
        bandwidth = Bandwidth.classic(
            rateLimitingProperties.capacity(),
            Refill.greedy(rateLimitingProperties.refillRate(), Duration.ofSeconds(
                rateLimitingProperties.refillTimeSeconds()))
        );
    }

    private Bucket buildNewBucket() {
        return Bucket.builder().addLimit(bandwidth).build();
    }
}
