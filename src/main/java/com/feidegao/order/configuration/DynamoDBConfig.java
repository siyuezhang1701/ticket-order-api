package com.feidegao.order.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DynamoDBConfig {

    @Bean
    @Profile("local")
    public AmazonDynamoDB localDynamoDB(
            @Value("${dynamodb.endpoint_url}") String dynamodbEndpoint,
            @Value("${aws.region}") String region
    ) {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                dynamodbEndpoint,
                                region
                        )
                )
                .withCredentials(
                        new AWSStaticCredentialsProvider(new BasicAWSCredentials("dummy", "dummy"))
                )
                .build();
    }

    @Bean
    public DynamoDBMapper defaultMapper(AmazonDynamoDB dynamoDB) {
        return new DynamoDBMapper(dynamoDB);
    }
}
