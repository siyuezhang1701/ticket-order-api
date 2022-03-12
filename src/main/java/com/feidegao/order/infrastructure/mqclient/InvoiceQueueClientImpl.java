package com.feidegao.order.infrastructure.mqclient;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feidegao.order.message.InvoiceMessage;
import com.feidegao.order.mqclient.InvoiceQueueClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvoiceQueueClientImpl implements InvoiceQueueClient {

     private final String endpoint;

    private final AmazonSQS amazonSQS;

    private final ObjectMapper objectMapper;

    private static final String QUEUE_NAME = "invoice-queue.fifo";

    public InvoiceQueueClientImpl(AmazonSQS amazonSQS, @Value("${aws.sqs.endpoint_url}") String endpoint, ObjectMapper objectMapper) {
        this.amazonSQS = amazonSQS;
        this.endpoint = endpoint;
        this.objectMapper = objectMapper;
    }

    @Override
    public void pushRequest(String title, float amount, String callbak) {
        try {
            SendMessageRequest sendMessageRequest = new SendMessageRequest()
                    .withQueueUrl(String.format("%s/%s", this.endpoint, QUEUE_NAME))
                    .withMessageBody(objectMapper.writeValueAsString(InvoiceMessage.builder()
                            .callback(callbak)
                            .title(title)
                            .amount(amount)
                            .build()));
            amazonSQS.sendMessage(sendMessageRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
