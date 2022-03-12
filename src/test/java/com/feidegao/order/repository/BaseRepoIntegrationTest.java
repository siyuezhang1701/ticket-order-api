package com.feidegao.order.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.feidegao.order.entity.DemoEntity;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@Import({TestConfig.class, LocalStackConfig.class})
public class BaseRepoIntegrationTest {


    protected void createDemoTable(
            DynamoDBMapper dynamoDBMapper,
            AmazonDynamoDB dynamoDB
    ) {

        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(
                DemoEntity.class
        );
        createTableRequest.setBillingMode(
                BillingMode.PAY_PER_REQUEST.toString()
        );
        dynamoDB.createTable(createTableRequest);
    }
}
