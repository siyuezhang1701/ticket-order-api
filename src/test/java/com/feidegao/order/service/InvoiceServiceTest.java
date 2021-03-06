package com.feidegao.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.feidegao.order.model.InsuranceRequest;
import com.feidegao.order.model.PaymentConfirmation;
import com.feidegao.order.model.PaymentRequest;
import com.feidegao.order.mqclient.InvoiceQueueClient;
import com.feidegao.order.repository.InvoiceRepository;
import com.feidegao.order.service.exception.MQMessageFailedException;
import com.feidegao.order.serviceclient.FlightClient;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private FlightClient flightClint;

    @Mock
    private InvoiceQueueClient invoiceQueueClient;

    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceService(orderRepository, proposalRepository, flightClint, invoiceQueueClient, invoiceRepository);
    }

    @Test
    void should_throw_exception_when_create_invoice_request_for_not_existed_order() {
        when(orderRepository.getOrderById(eq("1"))).thenReturn(null);

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1", "title"));
        assertEquals("the order is not existed", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_create_invoice_request_for_not_existed_ticket() {
        Order order = Order.builder()
                .id("1")
                .tickets(List.of(Ticket.builder().id("2").build()))
                .build();

        when(orderRepository.getOrderById(eq("1"))).thenReturn(order);

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1", "title"));
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

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1", "title"));
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

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1", "title"));
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

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1", "title"));
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

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1", "title"));
        assertEquals("the ticket is rebooked", exception.getMessage());
    }

    @Test
    void should_throw_exception_create_invoice_request_for_order_which_is_not_payed() {
        Order order = Order.builder()
                .id("1")
                .tickets(List.of(
                        Ticket.builder().id("1").flightNo("CA111").build()
                ))
                .paymentRequest(PaymentRequest.builder().build())
                .build();
        when(orderRepository.getOrderById(eq("1"))).thenReturn(order);

        when(proposalRepository.getByOriginTicketId(eq("1"))).thenReturn(null);

        Flight flight = Flight.builder().status(FlightStatus.FINISH).build();
        when(flightClint.getFlight(eq("CA111"))).thenReturn(flight);

        InvalidInvoiceRequestException exception = assertThrows(InvalidInvoiceRequestException.class, () -> invoiceService.requestInvoice("1", "1", "title"));
        assertEquals("the order has not been payed", exception.getMessage());
    }

    @Test
    void should_return_invoice_request_when_create_successfully() throws JsonProcessingException {
        Order order = Order.builder()
                .id("1")
                .tickets(List.of(
                        Ticket.builder().id("1").flightNo("CA111").amount(900).insuranceRequest(InsuranceRequest.builder().amount(100f).build()).build()
                ))
                .paymentRequest(PaymentRequest.builder().confirmation(PaymentConfirmation.builder().build()).build())
                .build();
        when(orderRepository.getOrderById(eq("1"))).thenReturn(order);
        when(proposalRepository.getByOriginTicketId(eq("1"))).thenReturn(null);

        Flight flight = Flight.builder().status(FlightStatus.FINISH).build();
        when(flightClint.getFlight(eq("CA111"))).thenReturn(flight);

        when(invoiceRepository.createInvoiceRequest(eq("1"), eq("1"))).thenReturn("1");

       assertEquals("1", invoiceService.requestInvoice("1", "1", "title"));

        verify(invoiceQueueClient, times(1))
                .pushRequest(eq("title"), eq(1000f), eq("/orders/1/tickets/1/invoiceRequest/confirmation"));

        verify(invoiceRepository, times(1)).createInvoiceRequest(eq("1"), eq("1"));

    }

    @Test
    void should_throw_exception_when_push_message_failed() throws JsonProcessingException {
        Order order = Order.builder()
                .id("1")
                .tickets(List.of(
                        Ticket.builder().id("1").flightNo("CA111").amount(900).insuranceRequest(InsuranceRequest.builder().amount(100f).build()).build()
                ))
                .paymentRequest(PaymentRequest.builder().confirmation(PaymentConfirmation.builder().build()).build())
                .build();
        when(orderRepository.getOrderById(eq("1"))).thenReturn(order);
        when(proposalRepository.getByOriginTicketId(eq("1"))).thenReturn(null);

        Flight flight = Flight.builder().status(FlightStatus.FINISH).build();
        when(flightClint.getFlight(eq("CA111"))).thenReturn(flight);

        doThrow(new RuntimeException("MQ is unreachable")).when(invoiceQueueClient).pushRequest(eq("title"), eq(1000f), eq("/orders/1/tickets/1/invoiceRequest/confirmation"));

        MQMessageFailedException exception = assertThrows(MQMessageFailedException.class, () -> invoiceService.requestInvoice("1", "1", "title"));

        assertEquals("MQ is unreachable", exception.getMessage());

        verify(invoiceRepository, times(0)).createInvoiceRequest(eq("1"), eq("1"));
    }
}
