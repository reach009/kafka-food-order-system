package com.example.orderservice.service;

import com.example.orderservice.dao.FoodOrderDao;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderReadyListener {
    private final FoodOrderDao orderDao;
    private final DashboardStreamService streamService;

    public OrderReadyListener(FoodOrderDao orderDao, DashboardStreamService streamService) {
        this.orderDao = orderDao;
        this.streamService = streamService;
    }

    @KafkaListener(topics = "food-orders-ready-topic", groupId = "order-service-group")
    public void handleOrderReadyEvent(String orderIdString) {
        try {
            Long orderId = Long.parseLong(orderIdString);
            orderDao.findById(orderId).ifPresent(order -> {
                order.setStatus("READY");
                orderDao.save(order);
                System.out.println("🔔 Order Service: Notified that Order " + orderId + " is now READY for pickup!");
                streamService.broadcastOrderUpdate("Order " + orderId + " is ready!");
            });
        } catch (NumberFormatException e) {
            System.err.println("Received invalid order ID: " + orderIdString);
        }
    }
}
