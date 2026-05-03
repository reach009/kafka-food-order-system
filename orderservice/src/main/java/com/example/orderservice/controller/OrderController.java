package com.example.orderservice.controller;

import com.example.orderservice.model.FoodOrder;
import com.example.orderservice.dao.FoodOrderDao;
import com.example.orderservice.service.DashboardStreamService;
import com.example.orderservice.service.OrderProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final FoodOrderDao orderDao;
    private final OrderProducer producer;
    private final DashboardStreamService streamService;

    public OrderController(FoodOrderDao orderDao, OrderProducer producer, DashboardStreamService streamService) {
        this.orderDao = orderDao;
        this.producer = producer;
        this.streamService = streamService;
    }

    @PostMapping
    public String placeOrder(@RequestBody FoodOrder order) {
        order.setStatus("PENDING");
        FoodOrder savedOrder = orderDao.save(order);
        String eventMessage = savedOrder.getId() + ":" + savedOrder.getFoodItem();
        producer.sendOrderEvent(eventMessage);
        return "Order " + savedOrder.getId() + " placed successfully!";
    }

    @GetMapping
    public List<FoodOrder> getActiveOrders() {
        return orderDao.findActiveOrders();
    }

    @GetMapping("/stream")
    public SseEmitter streamDashboardUpdates() {
        return streamService.subscribe();
    }

    @PutMapping("/{id}/pickup")
    public ResponseEntity<String> pickupOrder(@PathVariable Long id) {
        return orderDao.findById(id).map(order -> {
            if ("READY".equals(order.getStatus())) {
                order.setStatus("COMPLETED");
                orderDao.save(order);
                return ResponseEntity.ok("Order " + id + " has been picked up. Enjoy your meal!");
            } else {
                return ResponseEntity.badRequest().body("Order " + id + " is not ready yet! Current status: " + order.getStatus());
            }
        }).orElse(ResponseEntity.status(404).body("Oops! We couldn't find Order ID: " + id));
    }
}
