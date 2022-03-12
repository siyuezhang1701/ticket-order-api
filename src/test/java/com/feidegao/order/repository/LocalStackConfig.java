package com.feidegao.order.repository;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class LocalStackConfig {

    private LocalStackContainer localStackContainer;

    private static final DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:0.11.3");
    private static final LocalStackContainer.Service[] REQUIRED_SERVICES = {
            LocalStackContainer.Service.DYNAMODB,
            LocalStackContainer.Service.SQS,
    };

    @Bean
    public LocalStackContainer localStackContainer() {
        localStackContainer =new LocalStackContainer(localstackImage)
                .withServices(REQUIRED_SERVICES)
                .withEnv("HOSTNAME_EXTERNAL", "localhost");
        localStackContainer.start();

        return this.localStackContainer;
    }
}
