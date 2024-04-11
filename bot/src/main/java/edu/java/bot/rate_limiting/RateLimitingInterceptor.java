package edu.java.bot.rate_limiting;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor {
    private final LimitingCache limitingCache;

    @Override
    public boolean preHandle(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull Object handler
    ) throws Exception {
        Bucket tokenBucket = limitingCache.getBucket(request.getRemoteAddr());
        ConsumptionProbe consumptionProbe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if (consumptionProbe.isConsumed()) {
            long remainingTokens = consumptionProbe.getRemainingTokens();
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(remainingTokens));
            return true;
        }

        Duration waitDuration = Duration.ofNanos(consumptionProbe.getNanosToWaitForRefill());
        response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitDuration.toSeconds()));

        response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have exhausted your API request quota");

        return false;
    }
}
