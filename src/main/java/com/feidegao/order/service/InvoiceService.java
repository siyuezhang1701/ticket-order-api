package com.feidegao.order.service;

import com.feidegao.order.mqclient.InvoiceQueueClient;
import com.feidegao.order.repository.InvoiceRepository;
import com.feidegao.order.service.exception.MQMessageFailedException;
import com.feidegao.order.serviceclient.FlightClient;
import com.feidegao.order.model.Flight;
import com.feidegao.order.model.FlightStatus;
import com.feidegao.order.model.Order;
import com.feidegao.order.model.Ticket;
import com.feidegao.order.repository.OrderRepository;
import com.feidegao.order.repository.ProposalRepository;
import com.feidegao.order.service.exception.InvalidInvoiceRequestException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class InvoiceService {

    private final OrderRepository orderRepository;
    private final ProposalRepository proposalRepository;
    private final InvoiceQueueClient invoiceQueueClient;
    private final FlightClient flightClint;
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(OrderRepository orderRepository, ProposalRepository proposalRepository, FlightClient flightClint, InvoiceQueueClient invoiceQueueClient, InvoiceRepository invoiceRepository) {
        this.orderRepository = orderRepository;
        this.proposalRepository = proposalRepository;
        this.flightClint = flightClint;
        this.invoiceQueueClient = invoiceQueueClient;
        this.invoiceRepository = invoiceRepository;
    }

    public String requestInvoice(
            String orderId,
            String ticketId,
            String title){
        Order order = Optional.ofNullable(orderRepository.getOrderById(orderId))
                .orElseThrow(() -> new InvalidInvoiceRequestException("the order is not existed"));
        Ticket ticket = order.getTickets().stream().filter(t -> t.getId().equals(ticketId)).findFirst().orElseThrow(
                () -> new InvalidInvoiceRequestException("the ticket is not existed")
        );
        checkFlightStatus(ticket.getFlightNo());

        if (Objects.nonNull(ticket.getInvoiceRequest())){
            throw new InvalidInvoiceRequestException("the invoice request has been made");
        }

        if (Objects.nonNull(proposalRepository.getByOriginTicketId(ticketId))){
            throw new InvalidInvoiceRequestException("the ticket is rebooked");
        }
        String callback = String.format("/orders/%s/tickets/%s/invoiceRequest/confirmation", ticketId, orderId);
        try {
            invoiceQueueClient.pushRequest(title, ticket.getAmount() + ticket.getInsuranceRequest().getAmount(), callback);
        } catch (Exception exception) {
            throw new MQMessageFailedException(exception.getMessage());
        }

        return invoiceRepository.createInvoiceRequest(orderId, ticketId);
    }

    private void checkFlightStatus(String flightNo){
        Flight flight = flightClint.getFlight(flightNo);
        if (flight.getStatus().equals(FlightStatus.IN_FLIGHT))
            throw new InvalidInvoiceRequestException("the flight is in-flight");

        if (flight.getStatus().equals(FlightStatus.READY))
            throw new InvalidInvoiceRequestException("the flight has not taken off");
    }
}
