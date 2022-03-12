package com.feidegao.order.service;

import com.feidegao.order.model.Order;
import com.feidegao.order.repository.OrderRepository;
import com.feidegao.order.service.exception.InvalidInvoiceRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvoiceService {

    private final OrderRepository orderRepository;

    public InvoiceService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String requestInvoice(
            String orderId,
            String ticketId
    ){
        Order order = Optional.ofNullable(orderRepository.getOrderById(orderId))
                .orElseThrow(() -> new InvalidInvoiceRequestException("the order is not existed"));
        order.getTickets().stream().filter(ticket -> ticket.getId().equals(ticketId)).findFirst().orElseThrow(
                () -> new InvalidInvoiceRequestException("the ticket is not existed")
        );
        return "";
    }
}
