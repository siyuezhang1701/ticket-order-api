package com.feidegao.order.infrastructure;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.feidegao.order.infrastructure.LocalStackConfig;
import com.feidegao.order.infrastructure.TestConfig;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@Import({TestConfig.class, LocalStackConfig.class})
@SpringBootTest
public class BaseIntegrationTest {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private AmazonDynamoDB dynamoDB;
    @Autowired
    private AmazonSQS amazonSQS;


    protected void createTable(Class<?> clazz) {

        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(clazz);
        createTableRequest.setBillingMode(
                BillingMode.PAY_PER_REQUEST.toString()
        );
        dynamoDB.createTable(createTableRequest);
    }

    protected void createInvoiceQueue() {
        amazonSQS.createQueue("invoice-queue.fifo");
    }
}
