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
}
