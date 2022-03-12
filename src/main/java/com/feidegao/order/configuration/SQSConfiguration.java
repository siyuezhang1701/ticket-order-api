package com.feidegao.order.configuration;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SQSConfiguration {

    @Bean
    @Profile("local")
    public AmazonSQS amazonSQS(
            @Value("${aws.region}") String region
            ) {
        return AmazonSQSClientBuilder
            .standard()
                .withRegion(region)
            .withCredentials(InstanceProfileCredentialsProvider.getInstance())
            .build();
    }
}
