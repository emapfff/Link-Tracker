package edu.java.backoff;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

public class ConstantBackOff extends Retry {


    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
        return null;
    }
}
