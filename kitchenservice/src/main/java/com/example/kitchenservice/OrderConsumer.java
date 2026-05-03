package com.example.kitchenservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
class OrderConsumer {

    // 1. Create a Logger for this class
    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderConsumer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "food-orders-topic", groupId = "kitchen-group")
    public void consumeOrderEvent(String message) {
        // 2. Use log.info() instead of System.out.println()
        log.info("👨‍🍳 KITCHEN: Received raw message -> {}", message);

        String[] parts = message.split(":");
        String orderId = parts[0];
        String foodItem = parts.length > 1 ? parts[1] : "Unknown Item";

        // Let's simulate a system error! If someone orders "Poison", we throw an exception.
        if ("Poison".equalsIgnoreCase(foodItem.trim())) {
            log.error("🚨 KITCHEN: Hazardous material detected! Cannot cook {} for order {}.", foodItem, orderId);
            throw new RuntimeException("Kitchen refuses to cook Poison!");
        }

        log.info("Ticket #: {} | Preparing: {}", orderId, foodItem);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("Cooking process was interrupted for order {}", orderId, e);
            Thread.currentThread().interrupt();
        }

        log.info("✅ Order {} is ready for pickup!", orderId);
        kafkaTemplate.send("food-orders-ready-topic", orderId);
    }
}