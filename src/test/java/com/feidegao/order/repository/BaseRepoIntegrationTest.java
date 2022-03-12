package com.feidegao.order.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.feidegao.order.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@Import({TestConfig.class, LocalStackConfig.class})
@SpringBootTest
public class BaseRepoIntegrationTest {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private AmazonDynamoDB dynamoDB;

    protected void createOrderTable() {

        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(
                OrderEntity.class
        );
        createTableRequest.setBillingMode(
                BillingMode.PAY_PER_REQUEST.toString()
        );
        dynamoDB.createTable(createTableRequest);
    }
}
