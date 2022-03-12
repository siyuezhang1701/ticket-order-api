package com.feidegao.order.infrastructure.mqclient;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.feidegao.order.infrastructure.BaseIntegrationTest;
import com.feidegao.order.mqclient.InvoiceQueueClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvoiceQueueClientTest extends BaseIntegrationTest {

    @Autowired
    private InvoiceQueueClient invoiceQueueClient;

    @Autowired
    private AmazonSQS amazonSQS;

    @Value("${aws.sqs.endpoint_url}")
    private String queueUrl;

    @BeforeEach
    void setUp() {
        createInvoiceQueue();
    }

    @Test
    void should_push_message_to_queue() throws JsonProcessingException {
        invoiceQueueClient.pushRequest("title", 1000f, "callbackUrl");

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(String.format("%s/%s", this.queueUrl, "invoice-queue.fifo"));
        ReceiveMessageResult receiveMessageResult = amazonSQS.receiveMessage(receiveMessageRequest);
        assertEquals(1, receiveMessageResult.getMessages().size());
        assertEquals("{\"amount\":1000.0,\"title\":\"title\",\"callback\":\"callbackUrl\"}",receiveMessageResult.getMessages().get(0).getBody());
    }

    @Test
    void should_throw_exception_when_push_message_to_queue_failed(){
        amazonSQS.deleteQueue(String.format("%s/%s", this.queueUrl, "invoice-queue.fifo"));

        assertThrows(Exception.class, () -> invoiceQueueClient.pushRequest("title", 1000f, "callbackUrl"));
    }
}