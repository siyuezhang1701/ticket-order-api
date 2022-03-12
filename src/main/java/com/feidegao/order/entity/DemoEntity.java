package com.feidegao.order.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@DynamoDBTable(tableName = "demo-table")
@NoArgsConstructor
@AllArgsConstructor
public class DemoEntity {

    @DynamoDBHashKey
    private String id;
}
