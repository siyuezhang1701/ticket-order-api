package com.feidegao.order.controller;

import com.feidegao.order.service.InvoiceService;
import com.feidegao.order.service.exception.InvalidInvoiceRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/ticketOrders/{orderId}/tickets/{ticketId}/invoiceRequest")
    public ResponseEntity<String> createInvoiceRequest(
            @PathVariable String orderId,
            @PathVariable String ticketId
    ){
        try {
            return ResponseEntity.ok(invoiceService.requestInvoice(orderId, ticketId));
        } catch (InvalidInvoiceRequestException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }
}
