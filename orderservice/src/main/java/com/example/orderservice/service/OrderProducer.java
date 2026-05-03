package com.example.orderservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(String message) {
        System.out.println("Order Service: Sending order to kitchen -> " + message);
        kafkaTemplate.send("food-orders-topic", message);
    }
}
