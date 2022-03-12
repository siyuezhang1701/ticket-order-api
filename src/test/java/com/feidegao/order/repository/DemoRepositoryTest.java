package com.feidegao.order.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class DemoRepositoryTest extends BaseRepoIntegrationTest {
    @Autowired
    private DemoRepository demoRepository;

    @Autowired
    private DynamoDBMapper dbMapper;

    @Autowired
    private AmazonDynamoDB dynamoDB;

    @Test
    void should_insert_record_successfully() {
        this.createDemoTable(dbMapper, dynamoDB);
        assertNotNull(demoRepository.saveDemoEntity());
    }
}