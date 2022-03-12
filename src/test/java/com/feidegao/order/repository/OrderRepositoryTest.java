package com.feidegao.order.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.feidegao.order.entity.OrderEntity;
import com.feidegao.order.entity.TicketEntity;
import com.feidegao.order.model.Order;
import com.feidegao.order.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void should_return_order_with_tickets_info() {
        dynamoDBMapper.save(OrderEntity.builder()
                .id("1")
                        .tickets(List.of(TicketEntity.builder().id("2").build()))
                .build());

        Order expect = Order.builder()
                .id("1")
                .tickets(List.of(Ticket.builder().id("2").build()))
                .build();

        assertEquals(expect, orderRepository.getOrderById("1"));
    }
}
