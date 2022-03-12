package com.feidegao.order.service;

import com.feidegao.order.client.FlightClint;
import com.feidegao.order.model.Flight;
import com.feidegao.order.model.FlightStatus;
import com.feidegao.order.model.Order;
import com.feidegao.order.model.Ticket;
import com.feidegao.order.repository.OrderRepository;
import com.feidegao.order.service.exception.InvalidInvoiceRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private FlightClint flightClint;

    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceService(orderRepository, flightClint);
    }

    @Test
    void should_throw_exception_when_create_invoice_request_for_not_existed_order() {
        when(orderRepository.getOrderById(eq("1"))).thenReturn(null);

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1"));
        assertEquals("the order is not existed", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_create_invoice_request_for_not_existed_ticket() {
        Order order = Order.builder()
                .id("1")
                .tickets(List.of(Ticket.builder().id("2").build()))
                .build();

        when(orderRepository.getOrderById(eq("1"))).thenReturn(order);

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1"));
        assertEquals("the ticket is not existed", exception.getMessage());
    }

    @Test
    void should_throw_exception_create_invoice_request_for_flight_which_is_in_flight() {
        Order order = Order.builder()
                .id("1")
                .tickets(List.of(Ticket.builder().id("1").flightNo("CA111").build()))
                .build();
        when(orderRepository.getOrderById(eq("1"))).thenReturn(order);

        Flight flight = Flight.builder().status(FlightStatus.IN_FLIGHT).build();
        when(flightClint.getFlight(eq("CA111"))).thenReturn(flight);

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1"));
        assertEquals("the flight is in-flight", exception.getMessage());
    }
}
