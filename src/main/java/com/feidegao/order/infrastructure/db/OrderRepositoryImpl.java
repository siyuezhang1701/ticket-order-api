package com.feidegao.order.infrastructure.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.feidegao.order.entity.OrderEntity;
import com.feidegao.order.infrastructure.db.mapper.OrderMapper;
import com.feidegao.order.model.Order;
import com.feidegao.order.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final DynamoDBMapper mapper;

    public OrderRepositoryImpl(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Order getOrderById(String orderId) {
        DynamoDBQueryExpression<OrderEntity> query = new DynamoDBQueryExpression<>();
        query.withHashKeyValues(OrderEntity.builder().id(orderId).build());
        ArrayList<OrderEntity> res = new ArrayList<>(mapper.query(OrderEntity.class, query));
        return res.isEmpty() ? null : OrderMapper.MAPPER.mapToModel(res.get(0));
    }
}
