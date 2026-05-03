package com.example.kitchenservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlerConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaErrorHandlerConfig.class);

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
        // 1. The Recoverer: What to do when all retries are exhausted (Send to DLT)
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template,
                (record, exception) -> {
                    log.error("☠️ Moving failed message to DLQ. Topic: {}, Value: {}. Reason: {}",
                            record.topic(), record.value(), exception.getMessage());
                    // By default, it appends ".DLT" to the original topic name
                    return new org.apache.kafka.common.TopicPartition(record.topic() + ".DLT", record.partition());
                });

        // 2. The BackOff: How many times to retry, and how long to wait between retries
        // Here we wait 2 seconds between retries, and try a maximum of 2 times (3 total attempts)
        FixedBackOff backOff = new FixedBackOff(2000L, 2);

        // 3. Return the combined Error Handler
        return new DefaultErrorHandler(recoverer, backOff);
    }
}