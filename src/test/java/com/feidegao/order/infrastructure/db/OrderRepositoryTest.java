package com.feidegao.order.infrastructure.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.feidegao.order.entity.InvoiceRequestEntity;
import com.feidegao.order.entity.OrderEntity;
import com.feidegao.order.entity.TicketEntity;
import com.feidegao.order.model.InvoiceRequest;
import com.feidegao.order.model.Order;
import com.feidegao.order.model.Ticket;
import com.feidegao.order.repository.OrderRepository;
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
        createTable(OrderEntity.class);
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
                .tickets(List.of(TicketEntity.builder()
                        .flightNo("A111")
                        .invoiceRequest(InvoiceRequestEntity.builder().id("1").build())
                                .amount(900f)
                                .insuranceAmount(100f)
                        .id("2")
                        .build()))
                .build());

        Order expect = Order.builder()
                .id("1")
                .tickets(List.of(
                                Ticket.builder()
                                        .flightNo("A111")
                                        .id("2")
                                        .invoiceRequest(InvoiceRequest.builder().id("1").build())
                                        .amount(900f)
                                        .insuranceAmount(100f)
                                        .build()
                        )
                )
                .build();

        assertEquals(expect, orderRepository.getOrderById("1"));
    }
}
