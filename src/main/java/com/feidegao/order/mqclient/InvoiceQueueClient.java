package com.feidegao.order.mqclient;


public interface InvoiceQueueClient {
    void pushRequest(String title, float amount, String callbak);
}
