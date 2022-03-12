package com.feidegao.order.mqclient;


import com.fasterxml.jackson.core.JsonProcessingException;

public interface InvoiceQueueClient {
    void pushRequest(String title, float amount, String callbak) throws JsonProcessingException;
}
