package com.feidegao.order.service;

import com.feidegao.order.client.FlightClint;
import com.feidegao.order.model.Flight;
import com.feidegao.order.model.FlightStatus;
import com.feidegao.order.model.Order;
import com.feidegao.order.model.Ticket;
import com.feidegao.order.repository.OrderRepository;
import com.feidegao.order.service.exception.InvalidInvoiceRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvoiceService {

    private final OrderRepository orderRepository;

    private final FlightClint flightClint;

    public InvoiceService(OrderRepository orderRepository, FlightClint flightClint) {
        this.orderRepository = orderRepository;
        this.flightClint = flightClint;
    }

    public String requestInvoice(
            String orderId,
            String ticketId
    ){
        Order order = Optional.ofNullable(orderRepository.getOrderById(orderId))
                .orElseThrow(() -> new InvalidInvoiceRequestException("the order is not existed"));
        Ticket ticket = order.getTickets().stream().filter(t -> t.getId().equals(ticketId)).findFirst().orElseThrow(
                () -> new InvalidInvoiceRequestException("the ticket is not existed")
        );
        Flight flight = flightClint.getFlight(ticket.getFlightNo());
        if (flight.getStatus().equals(FlightStatus.IN_FLIGHT))
            throw new InvalidInvoiceRequestException("the flight is in-flight");
        return "";
    }
}
