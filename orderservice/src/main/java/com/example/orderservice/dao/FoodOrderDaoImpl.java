package com.example.orderservice.dao;

import com.example.orderservice.model.FoodOrder;
import com.example.orderservice.repository.FoodOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FoodOrderDaoImpl implements FoodOrderDao {

    private final FoodOrderRepository repository;

    public FoodOrderDaoImpl(FoodOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public FoodOrder save(FoodOrder order) {
        return repository.save(order);
    }

    @Override
    public List<FoodOrder> findActiveOrders() {
        return repository.findByStatusNot("COMPLETED");
    }

    @Override
    public Optional<FoodOrder> findById(Long id) {
        return repository.findById(id);
    }
}
