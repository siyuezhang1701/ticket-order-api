package com.feidegao.order.infrastructure.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.feidegao.order.entity.OrderEntity;
import com.feidegao.order.infrastructure.db.mapper.OrderMapper;
import com.feidegao.order.model.InvoiceRequest;
import com.feidegao.order.model.Order;
import com.feidegao.order.model.Ticket;
import com.feidegao.order.repository.InvoiceRepository;
import com.feidegao.order.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final OrderRepository orderRepository;
    private final DynamoDBMapper mapper;

    public InvoiceRepositoryImpl(OrderRepository orderRepository, DynamoDBMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public String createInvoiceRequest(String orderId, String ticketId) {
        Order order = orderRepository.getOrderById(orderId);
        InvoiceRequest invoiceRequest = InvoiceRequest.builder().id(UUID.randomUUID().toString()).build();
        List<Ticket> updatedTickets = order.getTickets().stream().map(t ->
                t.getId().equals(ticketId) ? t.toBuilder().invoiceRequest(invoiceRequest).build() : t
        ).collect(Collectors.toList());

        Order updatedOrder = order.toBuilder().tickets(updatedTickets).build();

        OrderEntity object = OrderMapper.MAPPER.mapToEntity(updatedOrder);
        mapper.save(object);

        return invoiceRequest.getId();
    }
}
