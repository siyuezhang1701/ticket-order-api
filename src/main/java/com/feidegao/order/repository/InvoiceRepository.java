package com.feidegao.order.repository;

public interface InvoiceRepository {
    String createInvoiceRequest(String orderId, String ticketId);
}
