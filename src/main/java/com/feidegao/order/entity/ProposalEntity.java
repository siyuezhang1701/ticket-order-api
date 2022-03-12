package com.feidegao.order.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@DynamoDBTable(tableName = "proposals")
@NoArgsConstructor
@AllArgsConstructor
public class ProposalEntity {
    public static final String ORIGIN_TICKET_ID_INDEX = "origin-ticket-index";

    @DynamoDBHashKey
    private String id;

    @DynamoDBIndexHashKey(globalSecondaryIndexNames = {ORIGIN_TICKET_ID_INDEX})
    private String originTicketId;

    private String s;
}
