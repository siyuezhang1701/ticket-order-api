package com.feidegao.order.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@DynamoDBTable(tableName = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @DynamoDBHashKey
    private String id;

    private List<TicketEntity> tickets;

    private PaymentRequestEntity paymentRequest;
}
