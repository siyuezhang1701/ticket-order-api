package com.feidegao.order.infrastructure.db;

import com.feidegao.order.repository.InvoiceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {
    @Override
    public String createInvoiceRequest(String orderId, String ticketId) {
        return null;
    }
}
