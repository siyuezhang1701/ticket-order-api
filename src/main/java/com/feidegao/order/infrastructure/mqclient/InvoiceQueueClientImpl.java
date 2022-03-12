package com.feidegao.order.infrastructure.mqclient;

import com.feidegao.order.mqclient.InvoiceQueueClient;
import org.springframework.stereotype.Component;

@Component
public class InvoiceQueueClientImpl implements InvoiceQueueClient {
    @Override
    public void pushRequest(String title, float amount, String callbak) {

    }
}
