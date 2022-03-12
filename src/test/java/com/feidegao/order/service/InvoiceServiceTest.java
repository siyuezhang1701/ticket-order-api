package com.feidegao.order.service;

import com.feidegao.order.client.FlightClient;
import com.feidegao.order.model.Flight;
import com.feidegao.order.model.FlightStatus;
import com.feidegao.order.model.InvoiceRequest;
import com.feidegao.order.model.Order;
import com.feidegao.order.model.Proposal;
import com.feidegao.order.model.Ticket;
import com.feidegao.order.repository.OrderRepository;
import com.feidegao.order.repository.ProposalRepository;
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
    private ProposalRepository proposalRepository;

    @Mock
    private FlightClient flightClint;

    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceService(orderRepository, proposalRepository, flightClint);
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

    @Test
    void should_throw_exception_create_invoice_request_for_flight_which_has_not_taken_off() {
        Order order = Order.builder()
                .id("1")
                .tickets(List.of(Ticket.builder().id("1").flightNo("CA111").build()))
                .build();
        when(orderRepository.getOrderById(eq("1"))).thenReturn(order);

        Flight flight = Flight.builder().status(FlightStatus.READY).build();
        when(flightClint.getFlight(eq("CA111"))).thenReturn(flight);

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1"));
        assertEquals("the flight has not taken off", exception.getMessage());
    }

    @Test
    void should_throw_exception_create_invoice_request_for_flight_which_already_has_invoice_request() {
        Order order = Order.builder()
                .id("1")
                .tickets(List.of(
                        Ticket.builder().id("1").flightNo("CA111").invoiceRequest(InvoiceRequest.builder().id("1").build()).build()
                ))
                .build();
        when(orderRepository.getOrderById(eq("1"))).thenReturn(order);

        Flight flight = Flight.builder().status(FlightStatus.FINISH).build();
        when(flightClint.getFlight(eq("CA111"))).thenReturn(flight);

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1"));
        assertEquals("the invoice request has been made", exception.getMessage());
    }

    @Test
    void should_throw_exception_create_invoice_request_for_ticket_which_is_rebooked() {
        Order order = Order.builder()
                .id("1")
                .tickets(List.of(
                        Ticket.builder().id("1").flightNo("CA111").build()
                ))
                .build();
        when(orderRepository.getOrderById(eq("1"))).thenReturn(order);

        Proposal proposal = Proposal.builder().id("2").originTicketId("1").build();
        when(proposalRepository.getByOriginTicketId(eq("1"))).thenReturn(proposal);

        Flight flight = Flight.builder().status(FlightStatus.FINISH).build();
        when(flightClint.getFlight(eq("CA111"))).thenReturn(flight);

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1"));
        assertEquals("the ticket is rebooked", exception.getMessage());
    }
}
