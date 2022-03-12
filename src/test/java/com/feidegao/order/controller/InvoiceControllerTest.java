package com.feidegao.order.controller;

import com.feidegao.order.service.InvoiceService;
import com.feidegao.order.service.exception.InvalidInvoiceRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InvoiceControllerTest extends BaseControllerIntegrationTest{
    @MockBean
    private InvoiceService mockInvoiceService;

    @Test
    void should_return_400_when_request_invoice_for_not_existed_order() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the order is not existed")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest"
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the order is not existed"));
    }

    @Test
    void should_return_400_when_request_invoice_for_not_existed_ticket() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the ticket is not existed")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest"
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the ticket is not existed"));
    }

    @Test
    void should_return_400_when_request_invoice_for_flight_which_is_in_flight() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the flight is in-flight")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest"
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the flight is in-flight"));
    }

    @Test
    void should_return_400_when_request_invoice_for_flight_which_has_not_taken_off() throws Exception {
        doThrow(new InvalidInvoiceRequestException("the flight has not taken off")).when(mockInvoiceService).requestInvoice(eq("1"), eq("1"));

        performPost(
                "/ticketOrders/1/tickets/1/invoiceRequest"
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("the flight has not taken off"));
    }
}
