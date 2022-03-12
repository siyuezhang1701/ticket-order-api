package com.feidegao.order.infrastructure.db;

import com.feidegao.order.model.Order;
import com.feidegao.order.repository.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @Override
    public Order getOrderById(String orderId) {
        return null;
    }
}
