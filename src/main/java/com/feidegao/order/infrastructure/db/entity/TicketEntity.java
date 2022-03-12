package com.feidegao.order.infrastructure.db.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class TicketEntity {

    private String id;
    private String flightNo;
    private InvoiceRequestEntity invoiceRequest;
    private float amount;
    private float insuranceAmount;
}
