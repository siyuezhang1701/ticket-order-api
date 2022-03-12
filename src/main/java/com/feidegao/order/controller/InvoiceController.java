package com.feidegao.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.feidegao.order.controller.resource.InvoiceRequestResource;
import com.feidegao.order.service.InvoiceService;
import com.feidegao.order.service.exception.InvalidInvoiceRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

@Controller
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/ticketOrders/{orderId}/tickets/{ticketId}/invoiceRequest")
    public ResponseEntity<String> createInvoiceRequest(
            @PathVariable String orderId,
            @PathVariable String ticketId,
            @RequestBody InvoiceRequestResource invoiceRequest
            ){
        try {
            invoiceService.requestInvoice(orderId, ticketId, invoiceRequest.getTitle());
            return ResponseEntity.created(URI.create(String.format("/ticketOrders/%s/tickets/%s/invoiceRequest", orderId, ticketId))).build();
        } catch (InvalidInvoiceRequestException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }
}
