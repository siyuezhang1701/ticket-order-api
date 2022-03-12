package com.feidegao.order.repository;

import com.feidegao.order.model.Order;

public interface OrderRepository {
    Order getOrderById(String orderId);
}
