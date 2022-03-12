package com.feidegao.order.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.feidegao.order.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNull;


public class OrderRepositoryTest extends BaseRepoIntegrationTest {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
   public void setUpData() {
        createOrderTable();
    }

    @Test
    void should_return_null_when_order_is_not_found_by_id() {
        dynamoDBMapper.save(OrderEntity.builder().id("notFoundId").build());

        assertNull(orderRepository.getOrderById("1"));
    }
}
