package com.feidegao.order.infrastructure.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.feidegao.order.entity.TicketEntity;
import com.feidegao.order.entity.OrderEntity;
import com.feidegao.order.entity.InvoiceRequestEntity;
import com.feidegao.order.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceRepositoryImplTest extends BaseRepoIntegrationTest {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    void setUp() {
        createTable(
                OrderEntity.class);
    }

    @Test
    void should_save_invoice_request() {
        dynamoDBMapper.save(OrderEntity.builder()
                .id("1")
                .tickets(List.of(TicketEntity.builder()
                        .flightNo("A111")
                        .amount(900f)
                        .insuranceAmount(100f)
                        .id("1")
                        .build()))
                .build());

        String requestId = invoiceRepository.createInvoiceRequest("1", "1");

        DynamoDBQueryExpression<OrderEntity> query = new DynamoDBQueryExpression<>();
        query.withHashKeyValues(OrderEntity.builder().id("1").build());
        OrderEntity res =  new ArrayList<>(dynamoDBMapper.query(OrderEntity.class, query)).get(0);

        InvoiceRequestEntity invoiceRequest = res.getTickets().get(0).getInvoiceRequest();
        assertNotNull(invoiceRequest);
        assertEquals(requestId, invoiceRequest.getId());

    }
}