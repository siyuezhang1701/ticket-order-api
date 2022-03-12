package com.feidegao.order.infrastructure;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;

@TestConfiguration
public class TestConfig {

    @Autowired
    private LocalStackContainer localStackContainer;

    @Bean
    public AmazonDynamoDB testDynamoDB() {
        return AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(
                    localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.DYNAMODB)
                    )
            .withCredentials(localStackContainer.getDefaultCredentialsProvider())
            .build();
    }

    @Bean
    public AmazonSQS testAmazonSQS(
    ) {
        System.setProperty("aws.sqs.endpoint_url", String.format("%s/000000000000",localStackContainer.getEndpointOverride(LocalStackContainer.Service.SQS).toString()));
        return AmazonSQSClientBuilder
                .standard()
                .withEndpointConfiguration(
                        localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS)
                )
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();
    }
}
