package com.feidegao.order.repository;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
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
}
