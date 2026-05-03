package com.example.orderservice.dao;

import com.example.orderservice.model.FoodOrder;
import java.util.List;
import java.util.Optional;

public interface FoodOrderDao {
    FoodOrder save(FoodOrder order);
    List<FoodOrder> findActiveOrders();
    Optional<FoodOrder> findById(Long id);
}
