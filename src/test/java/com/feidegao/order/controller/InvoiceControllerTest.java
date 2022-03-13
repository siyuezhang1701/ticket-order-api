package com.feidegao.order.controller;

import com.feidegao.order.controller.resource.InvoiceRequestResource;
import com.feidegao.order.service.InvoiceService;
import com.feidegao.order.service.exception.InvalidInvoiceRequestException;
import com.feidegao.order.service.exception.MQMessageFailedException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InvoiceControllerTest extends BaseControllerIntegrationTest{
    @MockBean
    private InvoiceService mockInvoiceService;

    @Test
    void should_return_400_when_request_invoice_for_not_existed_order() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the order is not existed")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"), eq("title"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest",
                InvoiceRequestResource.builder().title("title").build()
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the order is not existed"));
    }

    @Test
    void should_return_400_when_request_invoice_for_not_existed_ticket() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the ticket is not existed")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"), eq("title"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest",
                InvoiceRequestResource.builder().title("title").build()
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the ticket is not existed"));
    }

    @Test
    void should_return_400_when_request_invoice_for_flight_which_is_in_flight() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the flight is in-flight")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"), eq("title"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest",
                InvoiceRequestResource.builder().title("title").build()
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the flight is in-flight"));
    }

    @Test
    void should_return_400_when_request_invoice_for_flight_which_has_not_taken_off() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the flight has not taken off")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"), eq("title"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest",
                InvoiceRequestResource.builder().title("title").build()
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the flight has not taken off"));
    }

    @Test
    void should_return_400_when_request_invoice_for_flight_which_already_has_invoice_request() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the invoice request has been made")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"), eq("title"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest",
                InvoiceRequestResource.builder().title("title").build()
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the invoice request has been made"));
    }

    @Test
    void should_return_400_when_request_invoice_for_ticket_which_is_rebooked() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the ticket is rebooked")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"), eq("title"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest",
                InvoiceRequestResource.builder().title("title").build()
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the ticket is rebooked"));
    }

    @Test
    void should_return_400_when_request_invoice_for_order_which_is_not_payed() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the order has not been payed")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"), eq("title"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest",
                InvoiceRequestResource.builder().title("title").build()
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the order has not been payed"));
    }

    @Test
    void should_return_200_and_uri_when_request_invoice_for_ticket_successfully() throws Exception {
        when(mockInvoiceService.requestInvoice(eq("1"), eq("1"), eq("title"))).thenReturn("1");
        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest",
                InvoiceRequestResource.builder().title("title").build()
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/ticketOrders/1/tickets/1/invoiceRequest"));
    }

    @Test
    void should_return_500_when_push_mq_message_failed() throws Exception {
        doThrow(new MQMessageFailedException("MQ is unreachable")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"), eq("title"));
        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest",
                InvoiceRequestResource.builder().title("title").build()
        )
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("MQ is unreachable"));
    }

    @Test
    void should_return_400_when_create_invoice_request_without_title() throws Exception {
        doThrow(new MQMessageFailedException("MQ is unreachable")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"), eq("title"));
        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest",
                InvoiceRequestResource.builder().build()
        )
                .andExpect(status().isBadRequest());
    }
}