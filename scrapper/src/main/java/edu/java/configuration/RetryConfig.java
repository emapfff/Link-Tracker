//package edu.java.configuration;
//
//import edu.java.backoff.ConstantBackOff;
//import edu.java.backoff.ExponentialBackOff;
//import edu.java.backoff.LinearBackOff;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.validation.annotation.Validated;
//import reactor.util.retry.Retry;
//
//@Validated
//@Configuration
//@EnableConfigurationProperties(BackOffProperties.class)
//public class RetryConfig {
//    @Bean
//    @ConditionalOnProperty(prefix = "retry", name = "back-off-type", havingValue = "constant")
//    public Retry constantBackOff(BackOffProperties backOffProperties) {
//        return new ConstantBackOff(backOffProperties);
//    }
//
//    @Bean
//    @ConditionalOnProperty(prefix = "retry", name = "back-off-type", havingValue = "exponential")
//    public Retry exponentialBackOff(BackOffProperties backOffProperties) {
//        return new ExponentialBackOff(backOffProperties);
//    }
//
//    @Bean
//    @ConditionalOnProperty(prefix = "retry", name = "back-off-type", havingValue = "linear")
//    public Retry linearBackOff(BackOffProperties backOffProperties) {
//        return new LinearBackOff(backOffProperties);
//    }
//}
